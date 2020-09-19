package dataStructures;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FakeHashmap <K, V> {

    private int bucketCount = 16;
    private Bucket<K, V>[] buckets = new Bucket[bucketCount];

    public void insert(K key, V val){
        Node<K, V> node = new Node<>(key, val);
        Bucket<K, V> bucket;
        int ind = getIndex(key);
        if(buckets[ind] == null){
            bucket = new Bucket<>();
            buckets[ind] = bucket;
        }else {
            bucket = buckets[ind];
        }
        bucket.updateNode(node);
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

//    public void replace(K key, V oldVal, )

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

    public Set<Node> entrySet(){
        Bucket<K, V> bucket;
        Set<Node> outputSet = new HashSet<>();
        for (int i=0 ; i<bucketCount ; i++){
            bucket = buckets[i];
            if(bucket != null) {
                Set<Node<K, V>> set = bucket.getAll();
                outputSet.addAll(set);
            }
        }
        return outputSet;
    }

}

class Bucket<K, V>{
    private Node<K, V> start;
    private Node<K, V> end;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock rLock = lock.readLock();
    private Lock wLock = lock.writeLock();

    /*
     * adds new node if key is not present
     * updated value of node if key is present
     * */
    void updateNode(Node<K, V> node){
        wLock.lock();
        try {
            if (start == null) {
                start = node;
                end = node;
                return;
            }
            Node<K, V> oldNode = getNode(node.key);
            if (oldNode == null) {
                end.next = node;
                node.prev = end;
                end = node;
                return;
            }
            oldNode.val = node.val;
        }finally {
            wLock.unlock();
        }
    }

    /*
     * returns node if node.key.equals(key)
     * else returns null
     * */
    Node<K,V> getNode(K key){
        rLock.lock();
        try {
            Node<K, V> node = start;
            while (node != null) {
                if (node.key.equals(key)) {
                    return node;
                }
                node = node.next;
            }
            return null;
        }finally {
            rLock.unlock();
        }
    }


    Set<Node<K, V>> getAll(){
        Set<Node<K, V>> set = new HashSet<>();
        rLock.lock();
        try {
            Node<K, V> node = start;
            while (node != null){
                set.add(node);
                node = node.next;
            }
            return set;
        }finally {
            rLock.unlock();
        }
    }


    Node<K, V> delNode(K key){
        wLock.lock();
        try {
            Node<K, V> node = getNode(key);
            if (node != null) {
                if (start == node) {
                    start = node.next;
                }
                if (end == node) {
                    end = node.prev;
                }
                Node<K, V> nxtNode = node.next;
                Node<K, V> prevNode = node.prev;
                if (prevNode != null) {
                    prevNode.next = nxtNode;
                }
            }
            return node;
        }finally {
            wLock.unlock();
        }
    }
}
