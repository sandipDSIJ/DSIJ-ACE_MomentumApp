package in.dsij.acemomentum.model;

/**
 * Created by DSIJ_D1 on 4/13/2018.
 */

public class PdfWithPassword {
    String path;
    String sibscriptionno;
    String newpath;

    PdfWithPassword(String path, String sibscriptionno, String newpath)
    {
        this.path=path;
        this.sibscriptionno=sibscriptionno;
        this.newpath=newpath;
    }
}
