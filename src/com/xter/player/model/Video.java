package com.xter.player.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XTER on 2016/5/15.
 */
public class Video implements Parcelable {
	private long id;
	private String name;
	private String path;
	private int size;
	private String resolution;
	private String type;
	private long dateAdded;
	private int duration;
	private String thumb;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(long dateAdded) {
		this.dateAdded = dateAdded;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.name);
		dest.writeString(this.path);
		dest.writeInt(this.size);
		dest.writeString(this.resolution);
		dest.writeString(this.type);
		dest.writeLong(this.dateAdded);
		dest.writeInt(this.duration);
		dest.writeString(this.thumb);
	}

	public Video() {
	}

	protected Video(Parcel in) {
		this.id = in.readLong();
		this.name = in.readString();
		this.path = in.readString();
		this.size = in.readInt();
		this.resolution = in.readString();
		this.type = in.readString();
		this.dateAdded = in.readLong();
		this.duration = in.readInt();
		this.thumb = in.readString();
	}

	public static final Creator<Video> CREATOR = new Creator<Video>() {
		@Override
		public Video createFromParcel(Parcel source) {
			return new Video(source);
		}

		@Override
		public Video[] newArray(int size) {
			return new Video[size];
		}
	};
}
