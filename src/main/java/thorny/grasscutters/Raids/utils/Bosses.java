package thorny.grasscutters.Raids.utils;

import java.util.Set;

import com.google.gson.annotations.SerializedName;

import thorny.grasscutters.Raids.commands.RaidsCommand.SpawnParameters;

public class Bosses {

    @SerializedName("area")
    private int area;

    @SerializedName("id")
    private int id;

    @SerializedName("param")
    public SpawnParameters param;

    @SerializedName("pos")
    String pos;

    @SerializedName("rot")
    String rot;

    @SerializedName("groups")
    Set<Integer> groups;

    public Set<Integer> getGroups() {
        return groups;
    }

    public void setGroups(Set<Integer> groups) {
        this.groups = groups;
    }

    public String getRot() {
        return rot;
    }

    public void setRot(String rot) {
        this.rot = rot;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public SpawnParameters getParam() {
        return param;
    }

    public void setParam(SpawnParameters param) {
        this.param = param;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}