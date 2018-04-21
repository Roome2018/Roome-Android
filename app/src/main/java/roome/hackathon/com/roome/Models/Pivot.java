package roome.hackathon.com.roome.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lian on 20/04/2018.
 */

public class Pivot implements Parcelable{
    @SerializedName("room_id")
    @Expose
    private Integer roomId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    protected Pivot(Parcel in) {
        if (in.readByte() == 0) {
            roomId = null;
        } else {
            roomId = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
    }

    public static final Creator<Pivot> CREATOR = new Creator<Pivot>() {
        @Override
        public Pivot createFromParcel(Parcel in) {
            return new Pivot(in);
        }

        @Override
        public Pivot[] newArray(int size) {
            return new Pivot[size];
        }
    };

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (roomId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(roomId);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
    }
}
