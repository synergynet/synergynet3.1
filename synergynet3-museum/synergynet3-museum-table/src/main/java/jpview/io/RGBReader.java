/*
 * RGBReader.java
 *
 * Created on July 3, 2004, 11:41 PM
 */

package jpview.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.ProgressMonitorInputStream;

import jpview.Utils;
import jpview.ptms.PTM;
import jpview.ptms.RGBPTM;

/**
 * 
 * 
 * @author Default
 */
public class RGBReader implements PTMReader {

    private RGBPTM ptm;

    private String version = null;

    private ProgressMonitorInputStream __in;

    private boolean DEBUG = true;

    private boolean reset = true; /* assume a complete input stream */

    protected void reset(boolean b) {
        reset = b;
    }

    /** Creates a new instance of LRGBReader */
    public RGBReader(InputStream in) {
        __in = new ProgressMonitorInputStream(null, "Reading...", in);
        __in.getProgressMonitor().setMillisToDecideToPopup(0);
        __in.getProgressMonitor().setMillisToPopup(0);
    }

    public void setVersion(String s) {
        version = s;
    }

    public void setDebug(boolean b) {
        DEBUG = b;
    }

    private void debug(String s) {
        if (DEBUG)
            System.out.println(s);
    }

    public PTM readPTM() throws java.io.IOException {

        ptm = new RGBPTM();

        // try {

        if (reset) {
            version = PTMIO.getLine(__in);
            debug("Version: " + version);
            String type = PTMIO.getLine(__in);
            debug("Type: " + type);
        }

        /* read headers from this stream */
        // BufferedReader reader = new BufferedReader(new
        // InputStreamReader(__in));
        /* dimensions */
        ptm.setWidth(Integer.parseInt(PTMIO.getLine(__in)));
        ptm.setHeight(Integer.parseInt(PTMIO.getLine(__in)));

        debug("Width: " + ptm.getWidth());
        debug("Height: " + ptm.getHeight());

        String[] sa;

        /* scale */
        sa = PTMIO.getLine(__in).split(" ");
        float[] scale = new float[sa.length];
        for (int i = 0; i < sa.length; i++)
            scale[i] = Float.parseFloat(sa[i]);

        debug("Scale: " + Utils.asString(scale));

        /* bias */
        sa = PTMIO.getLine(__in).split(" ");
        int[] bias = new int[sa.length];
        for (int i = 0; i < sa.length; i++)
            bias[i] = Integer.parseInt(sa[i]);

        debug("Bias: " + Utils.asString(bias));

        /*
         int[][] red = new int[ptm.getWidth() * ptm.getHeight()][6];
         int[][] green = new int[ptm.getWidth() * ptm.getHeight()][6];
         int[][] blue = new int[ptm.getWidth() * ptm.getHeight()][6];
         int[][][] colors = new int[][][] { red, green, blue };
         */

        int[][][] colors = new int[3][ptm.getWidth() * ptm.getHeight()][6];

        // int [] rgb = new int[BUFSIZ];
        int offset;

        /* coefficients */
        int RED = 0;
        int BLUE = 2;

        for (int block = RED; block <= BLUE; block++) {
            for (int h = ptm.getHeight() - 1; h >= 0; h--) {
                for (int w = 0; w < ptm.getWidth(); w++) {
                    offset = h * ptm.getWidth() + w;
                    for (int i = 0; i < 6; i++) {
                        int c = __in.read();
                        colors[block][offset][i] = (int) PTMIO.cFinal(c,
                                bias[i], scale[i]);
                    }
                }
            }
        }
        
        /*
         for (int h = ptm.getHeight() - 1; h >= 0; h--) {
          for (int w = 0; w < ptm.getWidth(); w++) {
           offset = h * ptm.getWidth() + w;
            for (int block = RED; block <= BLUE; block++) {
             for (int i = 0; i < 6; i++) {
              int c = __in.read();
              colors[block][offset][i] = (int) PTMIO.cFinal(c,bias[i], scale[i]);
             }
            }
          }
         }
         */

        ptm.setCoeff(colors);
        ptm.computeNormals();
        return ptm;

    }

    public static void main(String args[]) {
        try {
            RGBReader me = new RGBReader(new FileInputStream(new File(args[0])));
            me.readPTM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
