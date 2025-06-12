package oop1.section08.kadai3;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ValidatedTextField extends JTextField {
    private final InputValidator validator;

    public ValidatedTextField(InputValidator validator, int columns) {
        super(columns);
        this.validator = validator;

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    validator.validate(getText());
                    setBackground(Color.WHITE);
                } catch (ValidationException ex) {
                    setBackground(Color.PINK);
                    JOptionPane.showMessageDialog(
                        ValidatedTextField.this,
                        ex.getMessage(),
                        "入力エラー",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                setBackground(Color.WHITE);
            }
        });
    }
} 