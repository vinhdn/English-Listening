package effortlessenglish.estorm.vn.effortlessenglish.Models;

import org.json.JSONException;
import org.json.JSONObject;

import effortlessenglish.estorm.vn.effortlessenglish.Utils.Constants;

/**
 * Created by Vinh on 1/9/15.
 */
public class Lession extends Menu {

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    private boolean isOffline = false;


    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    private String largeImage;


    public Lession(){
        super();
    }

    public Lession(Models models){
        super(models);
    }

    public Lession(JSONObject jsonObject, Models parent) throws JSONException {
        super(jsonObject,parent);
        if(!jsonObject.isNull("Link")){
            setLink(Constants.BASE + "data/"+jsonObject.getString("Link"));
        }
        this.setType(TYPE_MODEL.LESSION);
    }
}
