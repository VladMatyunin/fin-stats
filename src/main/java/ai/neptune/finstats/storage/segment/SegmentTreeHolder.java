package ai.neptune.finstats.storage.segment;

public class SegmentTreeHolder {

    public SegmentTreeHolder(float[] data) {
        this.tree = new SegmentTree(data);
        numOfItems = data.length;
    }

    SegmentTree tree;

    int numOfItems;
}
