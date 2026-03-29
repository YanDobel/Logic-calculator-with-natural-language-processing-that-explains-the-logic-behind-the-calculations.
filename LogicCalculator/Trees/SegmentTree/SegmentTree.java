package Trees.SegmentTree;

public class SegmentTree {
    private int tree[];
    private int n;
    private int totalLevels;

    public SegmentTree(int array[], int p2n) {
        this.n = p2n;
        this.tree = new int[4 * n + 1];
        this.totalLevels = 0;
        int temp = n;
        while (temp > 1) {
            temp /= 2;
            totalLevels++;
        }
        build(array, 1, 0, n - 1, totalLevels);
    }
    public int getRoot() {
        return tree[1];
    }

    private int build(int[] array, int node, int start, int end, int lvl) {
        if (start == end) {
            tree[node] = array[start];
            return 0;
        }
        int mid = (start + end) / 2;
        build(array, 2 * node, start, mid, lvl - 1);
        build(array, 2 * node + 1, mid + 1, end, lvl - 1);

        if (lvl % 2 != 0) {
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        } else {
            tree[node] = tree[2 * node] - tree[2 * node + 1];
        }
        return lvl;
    }

    public int update(int idx, int val) {
        if (idx < 0 || idx >= n) return 0;
        return update(1, 0, n - 1, idx, val, totalLevels);
    }

    private int update(int node, int start, int end, int idx, int val, int lvl) {
        if (start == end) {
            tree[node] = val;
            return 0;
        }
        int mid = (start + end) / 2;

        if (idx <= mid) {
            update(2 * node, start, mid, idx, val, lvl - 1);
        } else {
            update(2 * node + 1, mid + 1, end, idx, val, lvl - 1);
        }

        if (lvl % 2 != 0) {
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        } else {
            tree[node] = tree[2 * node] - tree[2 * node + 1];
        }
        return lvl;
    }
}