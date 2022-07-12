import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
public class test {
    static ArrayList<Node> arrayList=new ArrayList();
    static int totalvalue =0;//字符长度
    static int totalkey =0;//字符种类
    static String strn="";//替换后的字符
    static HashMap text=new HashMap();//字符概率
    static HashMap repleace=new HashMap();//字符哈夫曼编码
    static class Node{
        Node left;
        Node right;
        Node parent;
        int weight;
        char aChar;
    }
    static int getmin(ArrayList<Node> list){//获取最小权重结点下标
        int temp=0;
        if (list.size()==0)
            return -1;
        Node node=list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (node.weight>list.get(i).weight){
                temp=i;
                node=list.get(i);
            }
        }
        return temp;
    }
    static ArrayList<Node> merge(ArrayList<Node> list){//合并两个最小权重结点
        Node node1=list.get(getmin(list));
        list.remove(getmin(list));
        Node node2=list.get(getmin(list));
        list.remove(getmin(list));
        Node node3=new Node();
        node3.left=node1;
        node3.right=node2;
        node3.weight=node1.weight+node2.weight;
        node1.parent=node3;
        node2.parent=node3;
        list.add(node3);
        return list;
    }
    static ArrayList<Node> haff(ArrayList<Node> list){//合并所有结点
        while (list.size()>1){
            list=merge(list);
        }
        return list;
    }
    static void print(Node node,String str){
        if (node==null)
            return;
        if (node.right==null){
            strn=strn+str;
            System.out.printf("概率 %.2f",node.weight*1.0/ totalvalue);
            System.out.println("\t字符 "+node.aChar+"\t编码 "+str);
            repleace.put(node.aChar,str);
        }
        print(node.left,str+"0");
        print(node.right,str+"1");

    }
    public static void main(String[] args) throws FileNotFoundException {
        String str="";//存储文本内容
        String input="huffmanin.txt";
        String out ="huffmanout.txt";
        File file=new File(input);
        PrintWriter output =new PrintWriter(out);
        if (!file.exists()){
            System.out.println("not exist");
        }
        Scanner scanner=new Scanner(file);
        while (scanner.hasNext()){
            String temp=scanner.next();
            str=str+temp;
        }
        System.out.println(str);
        for (int i = 0; i < str.length(); i++) {
            if (text.containsKey(str.charAt(i))){
                int nu= (int) text.get(str.charAt(i));
                text.put(str.charAt(i),nu+1);
            }
            else {
                text.put(str.charAt(i),1);
            }
        }
        char key;
        int value;
        Iterator it=text.keySet().iterator();
        while (it.hasNext()){
            key=(char)(it.next());
            value=(int)text.get(key);
            System.out.println("key "+key+"\t value "+value);
            Node node=new Node();
            node.weight=value;
            node.aChar=key;
            arrayList.add(node);
        }
        Node p= haff(arrayList).get(0);
        totalvalue=str.length();
        totalkey=text.size();
        print(p,"");
        String str2="";
        for (int i = 0; i < str.length(); i++) {
            str2=str2+repleace.get(str.charAt(i));
        }
        int k=0;
        while (totalkey>Math.pow(2,k)){
            k++;
        }
        int len1=k*str.length();
        int len2=str2.length();
        System.out.println("哈夫曼存储长度 "+len1+"\t普通存储长度 "+len2);
        System.out.printf("压缩率:%.2f%%",len2*100.0/len1);
        output.print(str2);
        output.close();
    }
}
