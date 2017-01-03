package net.zenjoy.photoeditor.entity;

public class GalleryAlbumEntity {

	public int id;
	public String image_path;
	public int bucket_id;
	public String bucket_name;
	public int count;

	public GalleryAlbumEntity(int id, String image_path, int bucket_id,
							  String bucket_name, int count) {
		super();
		this.id = id;
		this.image_path = image_path;
		this.bucket_id = bucket_id;
		this.bucket_name = bucket_name;
		this.count = count;
	}

}
