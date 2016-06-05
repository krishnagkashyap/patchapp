/*
DESCRIPTION
This class has method to restart OAM Admin and Managed Servers

MODIFIED	(MM/DD/YY)
   cwesley 09/21/15 - Add thread to check for running message to reduce
                     execution time.
Shikadam	01/05/2015  -  Added memory arguments while server start for R2PS3
*//*


package tutorial.mvc;

import oracle.fiat.common.QALogger;
import oracle.fiat.utils.FIATUtils;
import oracle.fiat.utils.TestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class ServerStopStart {
//	private static QALogger logger;
//	static TestUtils testUtils;
	public static BufferedWriter bw;
	public static File fileLoc;
	Process process;
	static File bootFile;
	public static FileWriter fw;

	public static void stopStartServer(HashMap paramMap,
			ArrayList<String> serverList, String fileseparator, String strDir) {
//		testUtils = new TestUtils(logger);
//		ResourceBundle rbl = ResourceBundle.getBundle(OAMConstants.TEST_PROPERTY_AMCONFIG);
		String oamVersion = rbl.getString("RELEASE_VERSION");
//		logger = new QALogger("oracle.oam.fiat", "oam" + fileseparator
//				+ "server1" + fileseparator + "logs" + fileseparator
//				+ "serverRestart.log", "INFO");

//		logger.setComponent("ServerStopStart");
//		logger.entering("ServerStopStart", null);

		final String stopManagedWL = "stopManagedWebLogic.sh";
		final String startManagedWL = "startManagedWebLogic.sh";
		final String stopAdminWL = "stopWebLogic.sh";
		final String startAdminWL = "startWebLogic.sh";
		final String domainHomeLoc = (String) paramMap.get("domainHomeLoc");
		String userId = (String) paramMap.get("userId");
		final String password = (String) paramMap.get("password");
		String commandTorun = "null";
		String outfilename = "null";
		Boolean chkMessage = true;
		String outfilecontent = "null";

		try {

			// Write userid, password in boot.properties for all servers
			// involving in stop/ start action

			for (int i = 0; i < serverList.size(); i++) {

				File theDir = new File(domainHomeLoc + "/servers/"
						+ serverList.get(i) + "/security");

				// if the directory does not exist, create it
				if (!theDir.exists()) {
					logger.log(Level.INFO, "ServerRestart",
							"creating directory: " + domainHomeLoc
									+ "/servers/" + serverList.get(i)
									+ "/security");
					boolean result = theDir.mkdir();
					if (result) {
						logger.log(Level.INFO, "stopStartServer",
								"Created direcorty to store credentials ");
					}

				}

				bootFile = new File(domainHomeLoc + "/servers/"
						+ serverList.get(i) + "/security/boot.properties");

				if (!bootFile.exists()) {
//					logger.log(Level.INFO, "stopStartServer",
//							"Creating file boot.properties file" + bootFile);
					bootFile.createNewFile();
				} else {
//					logger.log(Level.INFO, "stopStartServer",
//							"Boot file already exsists" + bootFile);
				}
				fw = new FileWriter(bootFile);
				bw = new BufferedWriter(fw);
				bw.write("#File created by WLS-start-stop code at "
						+ new Date());
				bw.newLine();
				bw.write("username=" + userId);
				bw.newLine();
				bw.write("password=" + password);
				bw.close();

			}
			// Block involving AdminServer and Managed Servers

			if (serverList.contains("AdminServer")) {
				// First stop all managed servers
				for (int i = 1; i < serverList.size(); i++) {
//					logger.log(Level.INFO, "ServerRestart", "serveris = "
//							+ serverList.get(i));
//					logger.log(Level.INFO, "stopStartServer", "++++ Stopping "
//							+ serverList.get(i) + " Server ++++");
					fileLoc = new File(paramMap.get("domainHomeLoc") + "/bin/"
							+ stopManagedWL + " " + serverList.get(i));
//					logger.log(Level.INFO, "ServerRestart", " fileLoc = "
//							+ fileLoc);
					outfilename = fileseparator + strDir + fileseparator
							+ stopManagedWL + "outfile" +i;
					outfilecontent = "stopping stopManagedWL";
					createoutfile(outfilename, outfilecontent);
					commandTorun = fileLoc + "  >> " + outfilename;
//					logger.log(Level.INFO, "stopStartServer", "commandTorun = "
//							+ commandTorun);
					Runtime rt01 = Runtime.getRuntime();
					String[] strcommand01 = { "/bin/sh", "-c", commandTorun };
					Process p01 = rt01.exec(strcommand01);
//					logger.log(Level.INFO, "ServerRestart",
//							" executed the commandTorun " + commandTorun);
				  Runnable stopMngdServerOut = 
				    new FileChecker(serverList.get(i) +"Stop", 
                            outfilename, "Stopping Derby Server");
				  Thread stopMngdServerThread = new Thread(stopMngdServerOut);
				  stopMngdServerThread.setDaemon(true);
				  stopMngdServerThread.setName(serverList.get(i) +"Stop");
				  stopMngdServerThread.start();
				  try {
				    stopMngdServerThread.join(55000);
				  } catch (InterruptedException e) {
				    System.out.println("Failed to find message for AdminServer stop");
//				    logger.log(Level.INFO, "ServerRestart",
//				        " AdminServer server not stopped ");
				    assert false : "Failed to find message for AdminServer stop";
				    throw e;
				  }                  
//					logger.log(Level.INFO, "stopStartServer", "**** "
//							+ serverList.get(i)
//							+ " Managed Server Stop Completed ****");
				}
				// Stop admin server
//				logger.log(Level.INFO, "stopStartServer",
//						"++++ Stopping Admin Server ++++");
				fileLoc = new File(paramMap.get("domainHomeLoc") + "/bin/"
						+ stopAdminWL);
//				logger.log(Level.INFO, "ServerRestart", " fileLoc = " + fileLoc);
				outfilename = fileseparator + strDir + fileseparator
						+ stopAdminWL + "outfile";
				outfilecontent = "stopAdminWL";
				createoutfile(outfilename, outfilecontent);
				commandTorun = fileLoc + "  >> " + outfilename;
//				logger.log(Level.INFO, "ServerRestart", "commandTorun = "
//						+ commandTorun);
				Runtime rt02 = Runtime.getRuntime();
				String[] strcommand02 = { "/bin/sh", "-c", commandTorun };
				Process p02 = rt02.exec(strcommand02);
//				logger.log(Level.INFO, "ServerRestart",
//						" executed the commandTorun " + commandTorun);
			  Runnable stopAdminServerOut = 
			    new FileChecker("AdminServerStop", outfilename, 
                          "Stopping Derby Server");
			  Thread stopAdminServerThread = new Thread(stopAdminServerOut);
			  stopAdminServerThread.setDaemon(true);
			  stopAdminServerThread.setName("AdminServerStop");
			  stopAdminServerThread.start();
			  try {
			    stopAdminServerThread.join(55000);
			  } catch (InterruptedException e) {
			    System.out.println("Failed to find message for AdminServer stop");
//			    logger.log(Level.INFO, "ServerRestart",
//			        " AdminServer server not stopped ");
			    assert false : "Failed to find message for AdminServer stop";
			    throw e;
			  }      
				logger.log(Level.INFO, "ServerRestart",
						"**** AdminServer Stop Completed ****");
				// Start admin server
				fileLoc = new File(paramMap.get("domainHomeLoc") + "/bin/"
							+ startAdminWL);
				outfilename = fileseparator + strDir + fileseparator
						+ startAdminWL + "outfile";
				outfilecontent = outfilename;
				createoutfile(outfilename, outfilecontent);
				logger.log(Level.INFO, "ServerRestart", "fileLoc = " + fileLoc);
				commandTorun = fileLoc + "  >> " + outfilename;
				logger.log(Level.INFO, "ServerRestart", "commandTorun = "
						+ commandTorun);
				Runtime rt03 = Runtime.getRuntime();
				String[] strcommand03 = { "/bin/sh", "-c", commandTorun };
				Process p03 = rt03.exec(strcommand03);
        Runnable checkAdminServerOut = 
          new FileChecker("AdminServerCheck", outfilename, 
                          "Started WebLogic Admin Server");
        Thread checkAdminServerThread = new Thread(checkAdminServerOut);
        checkAdminServerThread.setDaemon(true);
        checkAdminServerThread.setName("AdminServerCheck");
        checkAdminServerThread.start();
        try {
          checkAdminServerThread.join(400000);
        } catch (InterruptedException e) {
          System.out.println("Failed to find message for AdminServer start");
          logger.log(Level.INFO, "ServerRestart",
              " AdminServer server not started ");          
          assert false : "Failed to find message for AdminServer start";
          throw e;
        }
			  logger.log(Level.INFO, "ServerRestart", "**** "
			      + " AdminServer Server Start Completed ****");

				// Start all managed servers
				for (int j = 1; j < serverList.size(); j++) {
					logger.log(Level.INFO, "ServerRestart", "++++ Starting "
							+ serverList.get(j) + " Managed Server ++++");
					logger.log(Level.INFO, "ServerRestart", "server is= "
							+ serverList.get(j));
					fileLoc = new File(paramMap.get("domainHomeLoc") + "/bin/"
							+ startManagedWL + " " + serverList.get(j));

					outfilename = fileseparator + strDir + fileseparator
							+ startManagedWL + "outfile" +j;
					outfilecontent = outfilename;
					createoutfile(outfilename, outfilecontent);
					
					commandTorun = fileLoc + "  >> " + outfilename;
					logger.log(Level.INFO, "ServerRestart", "commandTorun = "
							+ commandTorun);
					Runtime rt04 = Runtime.getRuntime();
					String[] strcommand04 = { "/bin/sh", "-c", commandTorun };
					Process p04;
					p04 = rt04.exec(strcommand04);
				  Runnable checkManagedServerOut = 
				    new FileChecker(serverList.get(j) + "Check", outfilename, 
                            "Started WebLogic Managed Server");
				  Thread checkManagedServerThread = new Thread(checkManagedServerOut);
				  checkManagedServerThread.setDaemon(true);
				  checkManagedServerThread.setName(serverList.get(j) + "Check");
				  checkManagedServerThread.start();
				  try {
				    checkManagedServerThread.join(400000);
				  } catch (InterruptedException e) {
				    System.out.println("Failed to find message Managed Server start");
				    logger.log(Level.INFO, "ServerRestart",
				        " Managed Server server not started ");          
				    assert false : "Failed to find message for Managed Server start";
            throw e;
				  }
					logger.log(Level.INFO, "ServerRestart",
							" executed the commandTorun " + commandTorun);
				  logger.log(Level.INFO, "ServerRestart", "**** "
				      + serverList.get(j)
				      + " Managed Server Start Completed ****");
				}

				// Block involving Managed Servers only
			} else {
				logger.log(Level.INFO, "ServerRestart",
						" Adminserver restart is mandatory for IDContext");
				logger.log(Level.INFO, "ServerRestart",
						" Please add Adminserver to the serverList array ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.exiting("ServerStopStart");
		}

	}

	public static void createoutfile(String outfilename, String outfilecontent)

	throws Exception {
		try {
			logger.log(Level.INFO, "createoutfile",
					" Started the createoutfile method ... ");
			StringBuffer buff5 = new StringBuffer();

			logger.log(Level.INFO, "createoutfile", " outfilename = "
					+ outfilename);
			buff5.append("\n");
			buff5.append("outfilecontent ");
			buff5.append("\n");
			BufferedWriter output05 = new BufferedWriter(new FileWriter(
					outfilename));
			logger.log(Level.INFO, "createoutfile",
					" creating file outputfile " + outfilename);
			output05.write(buff5.toString());
			logger.log(Level.INFO, "createoutfile", " Created file outputfile "
					+ outfilename);
			output05.close();

			File f2 = new File(outfilename);
			if (f2.exists()) {
				logger.log(Level.INFO, "createoutfile", " outputfile "
						+ outfilename + " exists ");
			} else {
				logger.log(Level.INFO, "createoutfile", " outputfile "
						+ outfilename + " does not exists, exiting... ");
				assert false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			assert false;
		}
	}

		*/
/**
	 * A method called When CSA artifacts are being populated , WLS needs to
	 * restarted
	 * 
	 * @param csaDomainHome
	 *            : CSA domain name
	 * @throws Exception
	 *//*

	public static void stopStartWLSServer(String fileseparator,
			String domainHome) throws Exception {
		testUtils = new TestUtils(logger);
		logger = new QALogger("oracle.oam.fiat", "oam" + fileseparator
				+ "server1" + fileseparator + "logs" + fileseparator
				+ "WLSserverRestart.log", "INFO");
		logger.setComponent("stopStartWLSServer");
		logger.entering("stopStartWLSServer", null);

		String fileName = System.getProperty("user.dir") + fileseparator
				+ "startStop.sh";
		String commands = "cd " + domainHome + fileseparator + "bin"
				+ fileseparator + "\n" + "./stopWebLogic.sh " + "\n";
		TestUtils.writeShellScript(fileName, commands);
		FIATUtils.executeShellScript(fileName, logger);
	
		commands = "cd " + domainHome + fileseparator + "bin" + fileseparator
				+ "\n" + "./startWebLogic.sh > "
				+ "startWeblogic_testAutomation.log 2>&1 & " + "\n";
//		TestUtils.writeShellScript(fileName, commands);
//		FIATUtils.executeShellScript(fileName, logger);

	}
}


class FileChecker implements Runnable {
    private String threadName;
    private String fileLocation;
    private String message;

    public FileChecker(String threadName, String fileLocation, String message) {
        this.threadName = threadName;
        this.fileLocation = fileLocation;
        this.message = message;
    }

  public void run() {
    try {
      System.out.println(">>>>>Starting thread " + threadName + " at " + 
                         new Date());
      String strfile = "null";
      boolean msgFound = false;

      synchronized(fileLocation) {
        do {
          BufferedReader br1 = new BufferedReader(new FileReader(fileLocation));
            try {
              StringBuilder sb = new StringBuilder();
              String line = br1.readLine();
    
              while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br1.readLine();
              }
              strfile = sb.toString();
            } finally {
              br1.close();
            }
            System.out.println("Searching for message " + message + " in " + 
                               fileLocation);
            msgFound = strfile.contains(message);
            if (msgFound) {
              System.out.println(" " + fileLocation + " file contains = " + message);
            }
            System.out.println("Waiting 5 seconds before checking output again");
            Thread.sleep(5000);
        } while (!msgFound);
      }
    } catch (Exception e) {
      e.printStackTrace();
      } finally {
        System.out.println(">>>>>Exiting thread " + threadName + " at " + 
                                                  new Date());
      }
  }
}

*/
