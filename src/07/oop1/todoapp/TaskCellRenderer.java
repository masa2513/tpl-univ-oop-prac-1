package oop1.todoapp;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * タスクの表示をカスタマイズするためのセルレンダラー。
 * 完了状態に応じて表示スタイルを変更します。
 */
public class TaskCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        // 親クラスの実装を呼び出し、基本的なJLabelコンポーネントを取得
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        // Taskオブジェクトの場合、表示をカスタマイズ
        if (value instanceof Task) {
            Task task = (Task) value;
            label.setText(task.toString());

            // 完了状態に応じて表示スタイルを変更
            if (task.isCompleted()) {
                label.setForeground(Color.GRAY);
                Map<TextAttribute, Object> attributes = new HashMap<>(label.getFont().getAttributes());
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                label.setFont(label.getFont().deriveFont(attributes));
            } else {
                label.setForeground(list.getForeground());
                Map<TextAttribute, Object> attributes = new HashMap<>(label.getFont().getAttributes());
                attributes.put(TextAttribute.STRIKETHROUGH, false);
                label.setFont(label.getFont().deriveFont(attributes));
            }
        }

        return label;
    }
}