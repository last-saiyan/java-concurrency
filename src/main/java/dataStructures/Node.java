package dataStructures;

class Node<K, V>{
    Node<K, V> next;
    Node<K, V> prev;
    K key;
    V val;
    Node(K key, V val){
        this.val = val;
        this.key = key;
        next = null;
        prev = null;
    }
}