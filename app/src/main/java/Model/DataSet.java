package Model;

import java.util.Vector;

public class DataSet {
   private Vector imgurl;




    public DataSet(Vector imgurl) {
        this.imgurl = imgurl;
    }

    public DataSet() {
    }

    public void setImgurl(Vector imgurl) {

        this.imgurl = imgurl;
    }

    public Vector getImgurl() {

        return imgurl;
    }
}
