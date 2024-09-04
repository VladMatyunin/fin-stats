package ai.neptune.finstats.storage.segment;

// Segment Tree Class
public class SegmentTree {
    // Root of the segment tree
    private final SegmentTreeNode root;

    public SegmentTree(float[] nums) {
        root = buildTree(nums, 0, nums.length - 1);
    }

    // Build the segment tree recursively
    private SegmentTreeNode buildTree(float[] nums, int start, int end) {
        if (start > end) {
            return null; // Empty node
        }
        SegmentTreeNode node = new SegmentTreeNode(start, end);
        if (start == end) {
            // Leaf node: store the value directly
            node.stats = Stats.fromValue(nums[start]);
        } else {
            int mid = start + (end - start) / 2;

            // Build left subtree
            node.left = buildTree(nums, start, mid);

            // Build right subtree
            node.right = buildTree(nums, mid + 1, end);

            // Combine values from children
            node.stats = node.left.stats.merge(node.right.stats);
        }
        return node;
    }

    public Stats getTotal() {
        return root.stats;
    }

    // Query the range sum [i, j]
    public Stats rangeStats(int i, int j) {
        return rangeStats(root, i, j);
    }

    private Stats rangeStats(SegmentTreeNode node, int start, int end) {
        if (node == null || start > node.end || end < node.start) {
            // Out of range or null node
            return Stats.empty();
        }
        if (start <= node.start && end >= node.end) {
            // Fully covered by this node
            return node.stats;
        }
        return rangeStats(node.left, start, end).merge(rangeStats(node.right, start, end));
    }
}

