package thorny.grasscutters.Raids.utils;

import java.util.Set;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import thorny.grasscutters.Raids.commands.RaidsCommand.SpawnParameters;

@Data
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
}