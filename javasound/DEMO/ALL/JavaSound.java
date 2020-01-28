/*
 * @(#)JavaSound.java	1.1.1.3  99/09/08
 *
 * Copyright (c) 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.File;
import java.util.Vector;
import javax.media.sound.sampled.*;
import javax.media.sound.midi.*;


/**
 * The Java Sound Samples : MidiSynth, Juke, CapturePlayback, Groove.
 *
 * @version @(#)JavaSound.java	1.1.1.3 99/09/08
 * @author Brian Lichtenwalter  
 */
public class JavaSound extends JFrame implements ChangeListener, ItemListener {

    Vector demos = new Vector(4);
    ReceiverItem receivers[];
    JTabbedPane tabPane = new JTabbedPane();
    int width = 760, height = 490;
    int index, rNum;


    public JavaSound(String audioDirectory) {
        super("Java Sound Demo");

        JMenuBar menuBar = new JMenuBar();
        ButtonGroup group = new ButtonGroup();
        JMenu options = (JMenu) menuBar.add(new JMenu("Options"));
        MidiDevice.Info info[] = MidiSystem.getReceiverInfo();
        receivers = new ReceiverItem[info.length];
        for (int i = 0; i < info.length; i++) {
            Receiver receiver = MidiSystem.getReceiver(info[i]);
            receivers[i] = (ReceiverItem) options.add(
                new ReceiverItem(info[i].getDescription(), 
                                 receiver, 
                                 i == 0 ?  true : false));
            receivers[i].addItemListener(this);
            group.add(receivers[i]);
        }
        getContentPane().add(menuBar, BorderLayout.NORTH);

        tabPane.addChangeListener(this);

        EmptyBorder eb = new EmptyBorder(5,5,5,5);
        BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
        CompoundBorder cb = new CompoundBorder(eb,bb);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new CompoundBorder(cb,new EmptyBorder(0,0,100,0)));
        final Juke juke = new Juke(audioDirectory);
        p.add(juke);
        demos.add(juke);
        tabPane.addTab("Juke Box", p);

        p = new JPanel(new BorderLayout());
        p.setBorder(new CompoundBorder(cb,new EmptyBorder(0,0,100,0)));
        CapturePlayback capturePlayback = new CapturePlayback();
        demos.add(capturePlayback);
        p.add(capturePlayback);
        tabPane.addTab("Capture/Playback", p);

        MidiSynth midiSynth = new MidiSynth();
        demos.add(midiSynth);
        tabPane.addTab("Midi Synthesizer", midiSynth);

        p = new JPanel(new BorderLayout());
        p.setBorder(new CompoundBorder(cb,new EmptyBorder(0,0,5,20)));
        Groove groove = new Groove();
        demos.add(groove);
        p.add(groove);
        tabPane.addTab("Groove Box", p);

        getContentPane().add(tabPane, BorderLayout.CENTER);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width/2 - width/2, d.height/2 - height/2);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
            public void windowDeiconified(WindowEvent e) { 
                ((ControlContext) demos.get(index)).open(receivers[rNum].receiver);
            }
            public void windowIconified(WindowEvent e) { 
                ((ControlContext) demos.get(index)).close();
            }
        });
        pack();
    }


    public void stateChanged(ChangeEvent e) {
        ((ControlContext) demos.get(index)).close();
        System.gc();
        index = tabPane.getSelectedIndex();
        ((ControlContext) demos.get(index)).open(receivers[rNum].receiver);
    }


    public void itemStateChanged(ItemEvent e) {
        ReceiverItem item = (ReceiverItem) e.getSource();
        ((ControlContext) demos.get(index)).close();
        ((ControlContext) demos.get(index)).open(item.receiver);
    }


    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }


    /**
     * Storage for our Midi Receiver.
     */
    static class ReceiverItem extends JRadioButtonMenuItem {
        public Receiver receiver;
        public ReceiverItem(String name, Receiver receiver, boolean selected) {
            super(name, selected);
            this.receiver = receiver;
        }
    }

        
    

    public static void main(String[] args) {
        if (MidiSystem.getSequencer(null) == null) {
            System.err.println("MidiSystem Sequencer Unavailable, exiting!");
            System.exit(1);
        } else if (AudioSystem.getMixer(null) == null) {
            System.err.println("AudioSystem Unavailable, exiting!");
            System.exit(1);
        }
        String media = "audio";
        JavaSound js = new JavaSound(args.length == 0 ? media : args[0]);
        js.setVisible(true);
        if (args.length > 0) {
            File file = new File(args[0]);
            if (file == null && !file.isDirectory()) {
                System.out.println("usage: java JavaSound audioDirectory");
            } 
        }
    }
}
