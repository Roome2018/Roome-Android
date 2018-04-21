package roome.hackathon.com.roome.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lian on 20/04/2018.
 */

public class Item implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("location_address")
    @Expose
    private String locationAddress;
    @SerializedName("location_latitude")
    @Expose
    private Double locationLatitude;
    @SerializedName("location_longitude")
    @Expose
    private Double locationLongitude;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("max_tenants")
    @Expose
    private Integer maxTenants;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;
    @SerializedName("is_available")
    @Expose
    private boolean isAvailable;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("remain")
    @Expose
    private Integer remain;
    @SerializedName("tenants")
    @Expose
    private List<Tenant> tenants = null;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;

    protected Item(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        info = in.readString();
        owner = in.readString();
        locationAddress = in.readString();
        if (in.readByte() == 0) {
            locationLatitude = null;
        } else {
            locationLatitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            locationLongitude = null;
        } else {
            locationLongitude = in.readDouble();
        }
        price = in.readString();
        if (in.readByte() == 0) {
            maxTenants = null;
        } else {
            maxTenants = in.readInt();
        }
        if (in.readByte() == 0) {
            viewCount = null;
        } else {
            viewCount = in.readInt();
        }
        isAvailable = in.readByte() != 0;
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            remain = null;
        } else {
            remain = in.readInt();
        }
        tenants = in.createTypedArrayList(Tenant.CREATOR);
        media = in.createTypedArrayList(Medium.CREATOR);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getMaxTenants() {
        return maxTenants;
    }

    public void setMaxTenants(Integer maxTenants) {
        this.maxTenants = maxTenants;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(info);
        dest.writeString(owner);
        dest.writeString(locationAddress);
        if (locationLatitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(locationLatitude);
        }
        if (locationLongitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(locationLongitude);
        }
        dest.writeString(price);
        if (maxTenants == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxTenants);
        }
        if (viewCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(viewCount);
        }
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        if (remain == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(remain);
        }
        dest.writeTypedList(tenants);
        dest.writeTypedList(media);
    }
}
