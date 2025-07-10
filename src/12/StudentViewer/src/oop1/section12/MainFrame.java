package oop1.section12;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainFrame extends JFrame {

    private JTree tree;
    private JTable table;
    private DefaultTableModel tableModel;
    private Document document;

    public MainFrame() {
        initializeComponents();
        loadXMLData();
        setupLayout();
        setupEvents();
    }

    private void initializeComponents() {
        setTitle("生徒名簿アプリ - StudentViewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // JTree初期化
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("愛知工業大学");
        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        // JTable初期化
        String[] columnNames = {"学生ID", "氏名", "学年"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
    }

    private void loadXMLData() {
        File xmlFile = new File("university_data.xml");

        if (!xmlFile.exists()) {
            JOptionPane.showMessageDialog(this,
                    "university_data.xmlファイルが見つかりません。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            buildTree();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "XMLファイルの読み込みに失敗しました：" + e.getMessage(),
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buildTree() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        root.removeAllChildren();

        Element universityElement = document.getDocumentElement();
        String universityName = universityElement.getAttribute("name");
        root.setUserObject(universityName);

        NodeList faculties = universityElement.getElementsByTagName("faculty");

        for (int i = 0; i < faculties.getLength(); i++) {
            Element faculty = (Element) faculties.item(i);
            String facultyName = faculty.getAttribute("name");
            DefaultMutableTreeNode facultyNode = new DefaultMutableTreeNode(facultyName);
            root.add(facultyNode);

            NodeList departments = faculty.getElementsByTagName("department");

            for (int j = 0; j < departments.getLength(); j++) {
                Element department = (Element) departments.item(j);
                String departmentName = department.getAttribute("name");
                DefaultMutableTreeNode departmentNode = new DefaultMutableTreeNode(departmentName);
                facultyNode.add(departmentNode);

                // 生徒情報を保存するためのユーザーオブジェクト
                departmentNode.setUserObject(new DepartmentInfo(departmentName, department));
            }
        }

        ((DefaultTreeModel) tree.getModel()).reload();

        // ツリーを展開
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // 左側のパネル（JTree）
        JPanel leftPanel = new JPanel(new BorderLayout());
        // leftPanel.setBorder(BorderFactory.createTitledBorder("学部・学科"));
        leftPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(300, 0));

        // 右側のパネル（JTable）
        JPanel rightPanel = new JPanel(new BorderLayout());
        // rightPanel.setBorder(BorderFactory.createTitledBorder("生徒情報"));
        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // 分割パネル
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);

        add(splitPane, BorderLayout.CENTER);

        // ステータスバー
        JLabel statusLabel = new JLabel("生徒情報を表示するには、学科または専攻を選択してください。");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        tree.addTreeSelectionListener(e -> {
            TreePath path = tree.getSelectionPath();
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object userObject = node.getUserObject();

                if (userObject instanceof DepartmentInfo) {
                    DepartmentInfo departmentInfo = (DepartmentInfo) userObject;
                    displayStudents(departmentInfo.getDepartmentElement());
                } else {
                    // 学部が選択された場合はテーブルをクリア
                    tableModel.setRowCount(0);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void displayStudents(Element departmentElement) {
        tableModel.setRowCount(0);

        NodeList students = departmentElement.getElementsByTagName("student");

        for (int i = 0; i < students.getLength(); i++) {
            Element student = (Element) students.item(i);
            String studentId = student.getAttribute("id");
            String name = getElementText(student, "name");
            String grade = getElementText(student, "grade");

            Object[] rowData = {studentId, name, grade + "年"};
            tableModel.addRow(rowData);
        }
    }

    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            Node node = nodes.item(0);
            if (node != null) {
                return node.getTextContent();
            }
        }
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // 学科情報を保持するクラス
    private static class DepartmentInfo {

        private String departmentName;
        private Element departmentElement;

        public DepartmentInfo(String departmentName, Element departmentElement) {
            this.departmentName = departmentName;
            this.departmentElement = departmentElement;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public Element getDepartmentElement() {
            return departmentElement;
        }

        @Override
        public String toString() {
            return departmentName;
        }
    }
}
