package ai.neptune.finstats.storage.segment;

public class SegmentTreeNode {
    // Range of indices covered by this node
    int start, end;

    Stats stats;

    // Pointers to left and right child nodes
    SegmentTreeNode left, right;

    public SegmentTreeNode(int start, int end) {
        this.start = start;
        this.end = end;
        this.left = null;
        this.right = null;
        this.stats = Stats.empty();
    }
}
