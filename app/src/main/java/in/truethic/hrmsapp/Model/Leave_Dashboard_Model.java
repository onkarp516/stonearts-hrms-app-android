package in.truethic.hrmsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leave_Dashboard_Model {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("leaves_allowed")
    @Expose
    private String leaves_allowed;

    @SerializedName("usedleaves")
    @Expose
    private String usedleaves;

    @SerializedName("remainingleaves")
    @Expose
    private String remainingleaves;

    public String getRemainingleaves() {
        return remainingleaves;
    }

    public void setRemainingleaves(String remainingleaves) {
        this.remainingleaves = remainingleaves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaves_allowed() {
        return leaves_allowed;
    }

    public void setLeaves_allowed(String leaves_allowed) {
        this.leaves_allowed = leaves_allowed;
    }

    public String getUsedleaves() {
        return usedleaves;
    }

    public void setUsedleaves(String usedleaves) {
        this.usedleaves = usedleaves;
    }
}
