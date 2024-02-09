package thorny.grasscutters.Raids.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("bosses")
    private List<Bosses> bosses;

    public List<Bosses> getBosses() {
        return bosses;
    }

    public void setBosses(List<Bosses> bosses) {
        this.bosses = bosses;
    }

    public void setDefault() {
        bosses = new ArrayList<>();
    }
}