package menus;

import frames.DrawingPanel;
import global.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FileMenu extends JMenu {
    private static final long serialVersionUID = 1L;

    private DrawingPanel drawingPanel;
    private File file;
    private File directory;

    public FileMenu(String menuLabel) {
        super(menuLabel);       // super는 여기서 JMenu를 일컫는다.
        this.file = null;
        this.directory = new File(Constants.EFileConstant.eFolderPath.getConstant());
        checkDefaultFolder();      // 결과 저장 folder 존재 확인

        ActionHandler actionHandler = new ActionHandler();
        for (Constants.EFileMenu eFileMenuItem : Constants.EFileMenu.values()) {
            JMenuItem menuItem = new JMenuItem(eFileMenuItem.getLabel());
            menuItem.addActionListener(actionHandler);
            menuItem.setActionCommand(eFileMenuItem.name());        // 이 버튼에 의해 발생하는 이벤트에 대한 명령 이름을 설정
            menuItem.setAccelerator(eFileMenuItem.getKeyStroke());      // 단축키 지정
            menuItem.setToolTipText(eFileMenuItem.getText());       // 툴팁 지정
            this.add(menuItem);
        }
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    private boolean checkSaveOrNot() {
        if (this.drawingPanel.isUpdated()) {       // 수정사항이 존재 O
            int answer = JOptionPane.showConfirmDialog(this.drawingPanel, "수정사항을 저장하시겠습니까?");
            if (answer == JOptionPane.OK_OPTION) {      // ok 응답
                this.save();
                return true;
            } else if (answer == JOptionPane.NO_OPTION) {   // no 응답
                return false;
            } else {        // cancel 응답
                return true;
            }
        } else return false;    // 수정사항 존재 X
        // checkSaveOrNot
        // true -> 수정있고 YES 응답, 수정있으나 CANCEL 응답
        // false -> 수정있으나 NO 응답, 수정사항이 없음
    }

    private void checkDefaultFolder() {
        File folder = new File(String.valueOf(this.directory));
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("폴더 생성완료");
        } else System.out.println("Path exists : myDrawingResult");
    }

    private void saveObject(File file) {     // == store()
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);       // byteStream -> file 으로 변환
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);       // Object -> byteStream 으로 변환해줌
            objectOutputStream.writeObject(this.drawingPanel.getShapes());
            objectOutputStream.close();     // write를 다했으니 닫아주자. object를 클로즈 시키면 파일도 클로즈 됨 (연결되었기 때문에)
            this.drawingPanel.setUpdated(false);       // 저장하였으니 수정여부는 다시 원상복구
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadObject(File file) {     // == load()
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();     // 오브젝트를 읽어주자 다형성으로 Object형으로 받음.
            this.drawingPanel.setShapes(object);        // 읽은 것을 drawingPanel에 setShapes해주자.
            objectInputStream.close();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newPanel() {
        if (!checkSaveOrNot()) {
            System.out.println("새 창 만들기");
            this.drawingPanel.reset();
        }
    }

    private void open() {
        if (!checkSaveOrNot()) {
            JFileChooser fileChooser = new JFileChooser(this.directory);
            int reply = fileChooser.showOpenDialog(this.drawingPanel);
            if (reply == JFileChooser.APPROVE_OPTION) {
                this.directory = fileChooser.getCurrentDirectory();
                this.file = fileChooser.getSelectedFile();
                this.loadObject(this.file);
            }
        }
    }

    private void save() {
        if (this.drawingPanel.isUpdated()) {
            if (this.file == null) {
                this.saveAs();
                System.out.println("다른 이름으로 저장");
            } else {
                this.saveObject(this.file);
                System.out.println("해당파일에 그대로 저장");
            }
        } else {    // 수정사항이 없을 시 저장 불가 안내창
            JOptionPane.showMessageDialog(this.drawingPanel, "수정된 내용이 없습니다!");
        }
    }

    private void saveAs() {
        JFileChooser fileChooser = new JFileChooser(this.directory);
        fileChooser.setDialogTitle("파일 저장");
        int reply = fileChooser.showSaveDialog(this.drawingPanel);      // 부모를 설정해주어야 해. 부모가 없어지면 이 다이얼로그도 없어지게끔.
        if (reply == JFileChooser.APPROVE_OPTION) {
            this.directory = fileChooser.getCurrentDirectory();
            this.file = fileChooser.getSelectedFile();      // 어떤 파일을 선택했는지 가져와야지
            this.saveObject(this.file);
        }
    }

    private void doPrint() {
        this.drawingPanel.doPrint();
    }

    private void quit() {
        int answer = JOptionPane.showConfirmDialog(this.drawingPanel, "정말 프로그램을 종료하시겠습니까?", "System Quit", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            System.out.println("시스템 종료하기");
            System.exit(0);     // 통상적으로 0이 아닌 status는 비정상적인 종료를 의미
        } else {
            System.out.println("실행을 취소합니다.");
        }
    }

    public void initialize() {

    }

    class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(Constants.EFileMenu.eNew.name())) {
                newPanel();
            } else if (e.getActionCommand().equals(Constants.EFileMenu.eOpen.name())) {
                open();
            } else if (e.getActionCommand().equals(Constants.EFileMenu.eSave.name())) {
                save();
            } else if (e.getActionCommand().equals(Constants.EFileMenu.eSaveAs.name())) {
                saveAs();
            } else if (e.getActionCommand().equals(Constants.EFileMenu.ePrint.name())) {
                doPrint();
            } else if (e.getActionCommand().equals(Constants.EFileMenu.eQuit.name())) {
                quit();
            }
        }
    }
}
