package jp.ac.ait.k24044.library;

//図書館の本を表すクラス
//ISBN、タイトル、著者、貸出状況を管理する
public class Book {
    //ISBNコード（読み取り専用）
    private final String isbn;
    //本のタイトル 
    private String title;
    //著者名
    private String author;
    //貸出状況（true: 貸出中、false: 貸出可能）
    private boolean isBorrowed;

    //本のコンストラクタ
    public Book(String isbn, String title, String author) {
        // ISBNの検証
        if (isbn == null || isbn.trim().isEmpty()) {
            System.err.println("警告: ISBNが無効です。デフォルト値を設定します。");
            this.isbn = "未設定";
        } else {
            this.isbn = isbn;
        }

        // タイトルの検証
        if (title == null || title.trim().isEmpty()) {
            System.err.println("警告: タイトルが無効です。デフォルト値を設定します。");
            this.title = "未設定";
        } else {
            this.title = title;
        }

        // 著者名の検証
        if (author == null || author.trim().isEmpty()) {
            System.err.println("警告: 著者が無効です。デフォルト値を設定します。");
            this.author = "未設定";
        } else {
            this.author = author;
        }

        this.isBorrowed = false;
    }

    //ISBNを取得
    public String getIsbn() {
        return isbn;
    }

    //タイトルを取得
    public String getTitle() {
        return title;
    }

    //著者名を取得
    public String getAuthor() {
        return author;
    }

    //貸出状況を取得
    public boolean isBorrowed() {
        return isBorrowed;
    }

    //本を貸し出す
    public boolean borrowBook() {
        if (isBorrowed) {
            System.err.println("この本は既に貸出中です。");
            return false;
        }
        isBorrowed = true;
        return true;
    }

    //本を返却する
    public boolean returnBook() {
        if (!isBorrowed) {
            System.err.println("この本は既に返却済みです。");
            return false;
        }
        isBorrowed = false;
        return true;
    }

    //本の詳細情報を文字列で取得
    public String getBookDetails() {
        return String.format("ISBN: %s, タイトル: %s, 著者: %s, 貸出状況: %s",
                isbn, title, author, isBorrowed ? "貸出中" : "貸出可能");
    }
} 