package jp.ac.ait.k24044.library;

//図書館の会員を表すクラス
//会員ID、名前、貸出中の本リスト、最大貸出冊数を管理する
public class LibraryMember {
    //会員ID（読み取り専用
    private final String memberId;
    //会員名
    private String name;
    //貸出中の本のリスト
    private Book[] borrowedBooks;
    //最大貸出可能冊数
    private int maxBorrowLimit;

    //会員のコンストラクタ（デフォルトの最大貸出冊数を使用）
    public LibraryMember(String memberId, String name) {
        this(memberId, name, 5);
    }

    //会員のコンストラクタ（最大貸出冊数を指定）
    public LibraryMember(String memberId, String name, int maxBorrowLimit) {
        this.memberId = memberId;
        this.name = name;
        if (maxBorrowLimit <= 0) {
            System.err.println("警告: 最大貸出冊数が無効です。デフォルト値（1冊）を設定します。");
            this.maxBorrowLimit = 1;
        } else {
            this.maxBorrowLimit = maxBorrowLimit;
        }
        this.borrowedBooks = new Book[this.maxBorrowLimit];
    }

    //会員IDを取得
    public String getMemberId() {
        return memberId;
    }

    //会員名を取得
    public String getName() {
        return name;
    }

    //最大貸出可能冊数を取得
    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

    //現在の貸出冊数を取得
    public int getCurrentBorrowCount() {
        int count = 0;
        for (Book book : borrowedBooks) {
            if (book != null) {
                count++;
            }
        }
        return count;
    }

    //さらに本を借りられるかどうかを確認
    public boolean canBorrowMore() {
        return getCurrentBorrowCount() < maxBorrowLimit;
    }

    //1冊の本を借りる
    public boolean borrowBook(Book book) {
        if (!canBorrowMore()) {
            System.err.println("貸出上限に達しています。");
            return false;
        }

        if (book.isBorrowed()) {
            System.err.println("この本は既に貸出中です。");
            return false;
        }

        if (book.borrowBook()) {
            for (int i = 0; i < borrowedBooks.length; i++) {
                if (borrowedBooks[i] == null) {
                    borrowedBooks[i] = book;
                    return true;
                }
            }
        }
        return false;
    }

    //複数の本を借りる
    public int borrowBooks(Book[] booksToBorrow) {
        int borrowedCount = 0;
        for (Book book : booksToBorrow) {
            if (borrowBook(book)) {
                borrowedCount++;
            }
        }
        return borrowedCount;
    }

    //本を返却する
    public boolean returnBook(Book book) {
        for (int i = 0; i < borrowedBooks.length; i++) {
            if (borrowedBooks[i] != null && borrowedBooks[i].getIsbn().equals(book.getIsbn())) {
                if (book.returnBook()) {
                    borrowedBooks[i] = null;
                    return true;
                }
            }
        }
        System.err.println("この本は借りていません。");
        return false;
    }

    //会員情報を表示する
    //会員ID、名前、最大貸出冊数、現在の貸出冊数、貸出中の本の詳細を表示
    public void displayMemberInfo() {
        System.out.println("会員ID: " + memberId);
        System.out.println("名前: " + name);
        System.out.println("最大貸出冊数: " + maxBorrowLimit);
        System.out.println("現在の貸出冊数: " + getCurrentBorrowCount());
        System.out.println("貸出中の本:");
        for (Book book : borrowedBooks) {
            if (book != null) {
                System.out.println("  - " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
            }
        }
    }
} 