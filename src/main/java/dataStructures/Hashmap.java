package dataStructures;

public class Hashmap <K, V>{

    private int bucketCount = 16;
    private Bucket<K, V>[] buckets = new Bucket[bucketCount];

    public void insert(K key, V val){
        Node<K, V> node = new Node<>(key, val);
        Bucket<K, V> bucket;
        int ind = getIndex(key);
        if(buckets[ind] == null){
            bucket = new Bucket<K, V>();
            buckets[ind] = bucket;
        }else {
            bucket = buckets[ind];
        }
        bucket.updNode(node);
    }

    public V get(K key){
        int ind = getIndex(key);
        if(buckets[ind] == null){
            return null;
        }
        Node<K, V> node = buckets[ind].getNode(key);
        if(node == null){
            return null;
        }
        return node.val;
    }

    public V delete(K key){
        int ind = getIndex(key);
        if(buckets[ind] == null){
            return null;
        }
        Node<K, V> node = buckets[ind].delNode(key);
        if(node == null){
            return null;
        }
        return node.val;
    }

    private int getIndex(K key){
        int hash = key.hashCode();
        return hash%bucketCount;
    }

}

class Bucket<K, V>{
    private Node<K, V> start;
    private Node<K, V> end;

    /*
     * adds new node if key is not present
     * updated value of node if key is present
     * */
    public void updNode(Node<K, V> node){
        if(start == null){
            start = node;
            end = node;
            return;
        }
        Node<K, V> oldNode = getNode(node.key);
        if(oldNode == null) {
            end.next = node;
            node.prev = end;
            end = node;
            return;
        }
        oldNode.val = node.val;
    }

    /*
     * returns node if node.key.equals(key)
     * else returns null
     * */
    public Node<K,V> getNode(K key){
        Node<K,V> node = start;
        while (node != null){
            if(node.key.equals(key)){
                return node;
            }
            node = node.next;
        }
        return null;
    }


    public Node<K, V> delNode(K key){
        Node<K,V> node = getNode(key);
        if(node != null){
            if (start == node){
                start = node.next;
            }
            if (end == node){
                end = node.prev;
            }
            Node<K, V> nxtNode = node.next;
            Node<K, V> prevNode = node.prev;
            if(prevNode != null){
                prevNode.next = nxtNode;
            }
        }
        return node;
    }
}

class Node<K, V>{
    Node<K, V> next;
    Node<K, V> prev;
    K key;
    V val;
    public Node(K key, V val){
        this.val = val;
        this.key = key;
        next = null;
        prev = null;
    }
}