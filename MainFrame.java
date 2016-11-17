import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by xilingyuli on 2016/10/17.
 * 主窗口
 */
public class MainFrame extends JFrame {

    JTextArea result;
    JTextField path;
    JButton button,button2;

    //更改ui样式
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
    }

    MainFrame()
    {
        super("基因编辑距离");

        path = new JTextField();

        path.setText("src/sequence.txt");
        path.setPreferredSize(new Dimension(150,25));
        button = new JButton("选择文件");
        button.setPreferredSize(new Dimension(120,25));
        //选择源码文件
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(path.getText());
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showDialog(path,"确定");
                File f = chooser.getSelectedFile();
                if(f!=null) {
                    path.setText(f.getPath());
                }
            }
        });



        button2 = new JButton("确定");
        button2.setPreferredSize(new Dimension(200,25));
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fileName = getFile();
                String sequence ="";
                try {
                    String encoding="GBK";
                    File file=new File(fileName);
                    if(file.isFile() && file.exists()){ //判断文件是否存
                    InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt = null;
                        while((lineTxt = bufferedReader.readLine()) != null){
                            sequence += (lineTxt)+'\n';
                        }
                        read.close();
                    }else{
                        System.out.println("找不到指定的文件");
                    }
                } catch (Exception e2) {
                    System.out.println("读取文件内容出错");
                    e2.printStackTrace();
                }
                String[] sequencelist = sequence.split("\n");
                String sequence0 = sequencelist[0];
                String sequence1 = sequencelist[1];
                int[][] matrix = new int[sequence0.length()+1][sequence1.length()+1];

                for (int i=0;i<sequence0.length()+1;i++){
                    matrix[i][0] = 0;
                }
                for (int i=0;i<sequence1.length()+1;i++){
                    matrix[0][i] = 0;
                }

                for (int i=1;i<sequence0.length()+1;i++){
                    for (int j=1;j<sequence1.length()+1;j++){
                        if(sequence0.charAt(i-1) == sequence1.charAt(j-1)) {
                            matrix[i][j] = matrix[i - 1][j - 1];
                        }else{
                            int min = matrix[i - 1][j - 1];
                            if (matrix[i][j - 1]<min) min = matrix[i][j - 1];
                            if (matrix[i-1][j]<min) min = matrix[i-1][j];
                            matrix[i][j] = min+1;
                        }
                    }
                }
                String res = "    ";
                for (int j=0;j<sequence1.length();j++){
                    res += sequence1.charAt(j)+" " ;
                }
                res += "\n  0 ";
                for (int j=0;j<sequence1.length();j++){
                    res += matrix[0][j] +" " ;
                }
                int maxDistance = 0;
                res += "\n";
                for (int i=1;i<sequence0.length()+1;i++){
                    res += sequence0.charAt(i-1)+" ";
                    for (int j=0;j<sequence1.length()+1;j++){
                        res += matrix[i][j] +" ";
                        if (matrix[i][j]>maxDistance)maxDistance=matrix[i][j];
                    }
                    res += '\n';
                }

                res += "编辑距离: "+maxDistance;

                //sequence += String.valueOf((a+b));
                setResult(res);
               // new File(filename);
            }
        });

        result = new JTextArea();
        result.setPreferredSize(new Dimension(450,400));

        setPreferredSize(new Dimension(500,550));
        setLayout(new FlowLayout());
        add(path);
        add(button);

        add(button2);
        add(result);
        pack();

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public String getFile()
    {
        return path.getText();
    }

    public void setResult(String results)
    {
        result.setText(results);
    }


}
