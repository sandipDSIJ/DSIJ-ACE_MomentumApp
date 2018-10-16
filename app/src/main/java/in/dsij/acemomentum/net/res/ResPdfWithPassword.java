package in.dsij.acemomentum.net.res;

/**
 * Created by DSIJ_D1 on 4/13/2018.
 */

public class ResPdfWithPassword {
    String path;
    String sibscriptionno;
    String newpath;

    public ResPdfWithPassword(String path, String sibscriptionno, String newpath)
    {
        this.path=path;
        this.sibscriptionno=sibscriptionno;
        this.newpath=newpath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSibscriptionno() {
        return sibscriptionno;
    }

    public void setSibscriptionno(String sibscriptionno) {
        this.sibscriptionno = sibscriptionno;
    }

    public String getNewpath() {
        return newpath;
    }

    public void setNewpath(String newpath) {
        this.newpath = newpath;
    }
}
