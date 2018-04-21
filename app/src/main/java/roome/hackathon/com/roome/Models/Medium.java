package roome.hackathon.com.roome.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lian on 20/04/2018.
 */

public class Medium implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("model_type")
    @Expose
    private String modelType;
    @SerializedName("model_id")
    @Expose
    private Integer modelId;
    @SerializedName("collection_name")
    @Expose
    private String collectionName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("disk")
    @Expose
    private String disk;
    @SerializedName("mime_type")
    @Expose
    private String mimeType;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("manipulations")
    @Expose
    private List<String> manipulations = null;
    @SerializedName("custom_properties")
    @Expose
    private List<String> customProperties = null;
    @SerializedName("order_column")
    @Expose
    private Integer orderColumn;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("url")
    @Expose
    private String url;

    protected Medium(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        modelType = in.readString();
        if (in.readByte() == 0) {
            modelId = null;
        } else {
            modelId = in.readInt();
        }
        collectionName = in.readString();
        name = in.readString();
        fileName = in.readString();
        disk = in.readString();
        mimeType = in.readString();
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readInt();
        }
        if (in.readByte() == 0) {
            orderColumn = null;
        } else {
            orderColumn = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        url = in.readString();
    }

    public static final Creator<Medium> CREATOR = new Creator<Medium>() {
        @Override
        public Medium createFromParcel(Parcel in) {
            return new Medium(in);
        }

        @Override
        public Medium[] newArray(int size) {
            return new Medium[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<String> getManipulations() {
        return manipulations;
    }

    public void setManipulations(List<String> manipulations) {
        this.manipulations = manipulations;
    }

    public List<String> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(List<String> customProperties) {
        this.customProperties = customProperties;
    }

    public Integer getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(Integer orderColumn) {
        this.orderColumn = orderColumn;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        dest.writeString(modelType);
        if (modelId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(modelId);
        }
        dest.writeString(collectionName);
        dest.writeString(name);
        dest.writeString(fileName);
        dest.writeString(disk);
        dest.writeString(mimeType);
        if (size == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(size);
        }
        if (orderColumn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(orderColumn);
        }
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(url);
    }
}
