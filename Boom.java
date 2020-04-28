package game;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


class Timer extends Thread{
 JLabel la;
 boolean flag = false;
 Timer(JLabel la){
  this.la = la;
 }
 void finish() {
  flag = true;
 }
 @Override
 public void run() {
  int n=0; 
  while(true) {
   la.setText("�ð�: "+Integer.toString(n));
   try {
    sleep(1000);
    if(flag == true)
     return;
   }catch(InterruptedException e) {return;}
   n++;
  }
 }
 
}
public class Boom extends JFrame{
 
 JButton start = new JButton("start");
 JButton exit = new JButton("exit"); 
 
 JButton[][] button = new JButton[15][15]; 
 int[][] counts = new int[15][15];
 JLabel la = new JLabel();
 JLabel bnum = new JLabel();
 int Bnum = 65; 
 int boom = 23; 
 Timer th;
 Container c = new Container();
 
 public Boom() {
  setTitle("����ã��");
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
  bnum.setText("����: "+Integer.toString(Bnum));
  bnum.setFont(new Font("Gothic",Font.BOLD,30));
  la.setFont(new Font("Gothic",Font.BOLD,30));
  
  JPanel p = new JPanel();
  c.setLayout(new BorderLayout());
  add(p,BorderLayout.NORTH);
  add(exit,BorderLayout.SOUTH);

  
  p.setLayout(new FlowLayout(FlowLayout.CENTER,100,6));
  p.add(bnum);
  p.add(start);
  p.add(la);

  
  th = new Timer(la);
  th.start();
  
  start.addActionListener(new MyActionListener());
  exit.addActionListener(new MyActionListener());
  
  c.setLayout(new GridLayout(15,15)); //15��15��
  
  for(int i=0; i<button.length; i++) {
   for(int j=0; j<button[i].length; j++) {
    button[i][j] = new JButton(" ");
    button[i][j].addActionListener(new MyActionListener());
    
    final int finI = i;
    final int finJ = j;
    button[i][j].addMouseListener(new MouseAdapter() {

     @Override
     public void mousePressed(MouseEvent e) {
      if(SwingUtilities.isRightMouseButton(e)){
       if(button[finI][finJ].getText().equals(" ")) { 
        button[finI][finJ].setText("��"); 
        Bnum--;
           } else if (button[finI][finJ].getText().equals("��")){ 
            button[finI][finJ].setText(" "); 
           Bnum++;
           } 
      }
      bnum.setText("����: "+Integer.toString(Bnum));
     }
     
    });
    c.add(button[i][j]);
    
   }
   
  }
  
  add(c,BorderLayout.CENTER);
  
  makeRandom(); 
  
  start.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
    th.interrupt();
    th = new Timer(la);
    th.start();
   }
   
  });
  exit.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
    new menu();
    setVisible(false);
   }
   
  });
  
  setSize(730,730);
  setVisible(true);
 }
 //�������� ���ڿ� ���������ڰ����� �˷��ִ� ���� �ֱ�
 void makeRandom() {
  ArrayList<Integer> list = new ArrayList<Integer>();
  for(int i=0; i<counts.length; i++) {
   for(int j=0; j<counts[i].length; j++)
    list.add(i*100+j); 
  }
  counts = new int[20][20]; //���ο� ������ ���� ī��Ʈ�� �ʱ�ȭ���ش�
  for(int i=0; i<Bnum; i++) { 
   int choice = (int) (Math.random() * list.size()); //����Ʈ�� ũ�⸸ŭ �������� ��������(0~����Ʈ��ũ��)
   counts[list.get(choice)/100][list.get(choice)%100] = boom;
   list.remove(choice); //������ �κ� ����Ʈ�� �ʱ�ȭ(�����ڸ��� ���ڷ� �����Ǹ� �ȵǹǷ�)
  }
  //���ڰ� �ƴѰ�� �������� ����
  for(int i=0; i<counts.length; i++) {
   for(int j=0; j<counts.length; j++) {
    int surround = 0;
    if(counts[i][j] != boom) {
     if(j>0 &&counts[i][j-1] == boom) //���ڰ� ��
      surround++;
     if(j<counts[i].length-1 && counts[i][j+1] == boom) //���ڰ� �Ʒ�
      surround++;
     if(i>0 && counts[i-1][j] == boom) //���ڰ� ���� 
      surround++;
     if(i<counts.length-1 && counts[i+1][j] == boom) //���ڰ� ������
      surround++;
     if(i>0 && j<counts.length-1 && counts[i-1][j+1] == boom) //���ڰ� ���ʾƷ��밢��
      surround++;
     if(i>0 && j>0 && counts[i-1][j-1] == boom) //���ڰ� �������밢��
      surround++;
     if(i<counts.length-1 && j<counts.length-1 && counts[i+1][j+1] == boom) //���ڰ� �����ʾƷ��밢�� 
      surround++;
     if(i<counts.length-1 && j>0 && counts[i+1][j-1] == boom) //���ڰ� ���������밢��
      surround++;
     
     counts[i][j] = surround;
    }
    
   }
  }
 }
 
 //���ӿ��� ���� ��
  void lostGame(){
   for(int i=0; i<button.length; i++) {
    for(int j=0; j<button[i].length; j++) {
     if(button[i][j].isEnabled()) {
      if(counts[i][j] != boom) //���ڰ� �ƴ� ���
       button[i][j].setText(Integer.toString(counts[i][j])); 
      
      else  //������ ���
       button[i][j].setText("��");
      
      button[i][j].setEnabled(false); //��Ȱ��
      th.finish();
     }
    }
   }
  }
 class MyActionListener implements ActionListener{
  @Override
  public void actionPerformed(ActionEvent e) {
   //start�� ������ ��
   if(e.getSource().equals(start)) {
    for(int i=0; i<button.length; i++) {
     for(int j=0; j< button.length; j++) {
      button[i][j].setEnabled(true);
      button[i][j].setText(" ");
      Bnum = 65;
      bnum.setText("����: "+Integer.toString(Bnum));
     }
    }
    makeRandom();
    
   }
   //����ã�� �������϶�
   else { 
         for (int i = 0; i < button.length ; i++) { 
          for (int j = 0; j < button[i].length; j++) { 
         if(e.getSource().equals(button[i][j])) {  //����ã�� ��ư�� ������ ��
            if (counts[i][j]== boom) { 
             lostGame(); 
            } 
          
            else { 
             button[i][j].setText(Integer.toString(counts[i][j])); 
             button[i][j].setEnabled(false); 
            }
           }
          }
       }
   }
  }
  
  
 }

}