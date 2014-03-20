package com.onebeartoe.internet.radio.services;

import com.onebeartoe.internet.radio.RadioModes;
import com.onebeartoe.internet.radio.Station;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * call the program like this
 * sudo LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib java -cp mpd-client-1.0-SNAPSHOT.jar com.onebeartoe.internet.radio.InternetRadio
 * 
 * 
 * The original command:
 * "LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib rtl_fm -M wbfm -f 101.1M | play -r 32k -t raw -e s -b 16 -c 1 -V1 -"
 *
 * @author Roberto Marquez
 */
public class RtlSdrAntennaRadioService extends OnebeartoeRadioService //implements InternetRadioStationService
{
    private Thread radioPlayer;
    private Process radioProcess;
    
    List<Station> defaultStations;
    
    public RtlSdrAntennaRadioService()
    {
        Station s = new Station();
        s.name = "Kono";
        s.frequency = 101.1;
        s.type = RadioModes.FM;
        defaultStations = new ArrayList();
        defaultStations.add(s);
        
        s = new Station();
        s.name = "Kzep";
        s.frequency = 104.5;
        s.type = RadioModes.FM;
        defaultStations.add(s);
    }

    @Override
    public boolean play() throws Exception 
    {
        System.out.println("play from antenna service - Started");
                
        radioPlayer = new Thread( new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {                    
                    System.out.println("play from antenna service  - Thread");

                    String command = "rtl_fm -M wbfm -f $$FREQUENCY$$M | play -r 32k -t raw -e s -b 16 -c 1 -V1 -";                
                    double frequency = 101.1;
//                    double frequency = 104.5;
                    command = command.replace("$$FREQUENCY$$", String.valueOf(frequency) );     
                    System.out.println("tuning into: " + command);
                    
                    // http://stackoverflow.com/questions/5928225/how-to-make-pipes-work-with-runtime-exec
                    String[] cmds = 
                    {
                        "/bin/sh",
                        "-c",
                        command
                    };
                    
                    String script = "/home/debian/play-radio.sh";
                    Runtime runtime = Runtime.getRuntime();
                    radioProcess = runtime.exec(script);
                    
//                    ProcessBuilder pb = new ProcessBuilder(cmds);
//                    radioProcess = pb.start();
                    
                    //                    Runtime runtime = Runtime.getRuntime();
                    //                    radioProcess = runtime.exec(cmds);
                    
                    //                    commandLine.execute(cmds);
                } 
                catch (IOException ex) 
                {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        });
        radioPlayer.start();
//        radioPlayer.run();
        System.out.println("play from antenna service - Done");
        
        return true;  // grr!
    }
    
    @Override
    public boolean playStation(String url) throws Exception 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Station> retreiveDefault() throws Exception 
    {
        return defaultStations;
    }

    @Override
    public List<Station> retreivePersonal() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void savePersonal(List<Station> stations) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
    
    @Override
    public void stopPlayback() throws Exception 
    {
        System.err.println("stop called from antenna service - 2");
        
        if(radioPlayer == null)
        {
            System.err.println("stop called from antenna service, on a null radio player");
        }
        else
        {
            System.out.println("stopping antenna radio - 7");
            
            
            OutputStream outputStream = radioProcess.getOutputStream();
            String ctrlZ = "\\x1a";// + "\n\r";
            outputStream.write(ctrlZ.getBytes());
            outputStream.flush();
            outputStream.close();
//            
            radioProcess.getInputStream().close();
            radioProcess.getErrorStream().close();
            
//            radioProcess.destroy();
            
//            radioPlayer.suspend();
//            radioPlayer.interrupt();
            
//            radioPlayer.wait();
        }
        
        System.err.println("stop called from antenna service - Done - 2");
    }

    @Override
    public List<String> verboseInformation() throws Exception 
    {
        System.out.println("rtl verbose information");
        
        String info = currentStation.toString();                
        List<String> output = new ArrayList();                
        output.add(info);
        
        return output;
    }



}
