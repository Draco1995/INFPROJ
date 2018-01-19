package Image;
public class TestBinaryImage {
	public static void main(String[] args) {
		// creating an image
		BinaryImage createdImg = new BinaryImage(256);
		createdImg.fillAreaWhite(0, 128, 128);
		createdImg.fillAreaWhite(128, 0, 128);
		new ImageViewer(createdImg);

		// loading an image
		BinaryImage loadedImg1 = new BinaryImage("resource/X.png"); new ImageViewer(loadedImg1);
		BinaryImage loadedImg2 = new BinaryImage("resource/bowtie.png"); new ImageViewer(loadedImg2);
		BinaryImage loadedImg3 = new BinaryImage("resource/christmasTree.png"); new ImageViewer(loadedImg3);
	}
}
