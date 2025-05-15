package jp.ac.ait.k24044.library;

public class LibrarySystemMain {
    public static void main(String[] args) {
        // 図書館の作成
        Library library = new Library();

        // 本の作成と登録
        Book[] books = new Book[10];
        books[0] = new Book("978-4-00-310101-8", "吾輩は猫である", "夏目漱石");
        books[1] = new Book("978-4-00-310102-5", "坊っちゃん", "夏目漱石");
        books[2] = new Book("978-4-00-310103-2", "こころ", "夏目漱石");
        books[3] = new Book("978-4-00-310104-9", "羅生門", "芥川龍之介");
        books[4] = new Book("978-4-00-310105-6", "蜘蛛の糸", "芥川龍之介");
        books[5] = new Book("978-4-00-310106-3", "人間失格", "太宰治");
        books[6] = new Book("978-4-00-310107-0", "斜陽", "太宰治");
        books[7] = new Book("978-4-00-310108-7", "銀河鉄道の夜", "宮沢賢治");
        books[8] = new Book("978-4-00-310109-4", "注文の多い料理店", "宮沢賢治");
        books[9] = new Book("978-4-00-310110-0", "走れメロス", "太宰治");

        for (Book book : books) {
            library.addBook(book);
        }

        // 会員の作成と登録
        LibraryMember member1 = new LibraryMember("M001", "山田太郎");
        LibraryMember member2 = new LibraryMember("M002", "鈴木花子", 3);

        library.registerMember(member1);
        library.registerMember(member2);

        // シナリオ1: 蔵書管理のテスト
        System.out.println("=== シナリオ1: 蔵書管理のテスト ===");
        library.displayAllBooks();
        
        // 本の削除テスト
        System.out.println("本の削除テスト:");
        library.removeBook("978-4-00-310101-8");
        library.displayAllBooks();

        // シナリオ2: 貸出・返却のテスト
        System.out.println("=== シナリオ2: 貸出・返却のテスト ===");
        library.lendBookToMember("M001", "978-4-00-310102-5");
        library.lendBookToMember("M001", "978-4-00-310103-2");
        library.lendBookToMember("M002", "978-4-00-310104-9");

        System.out.println("貸出後の状態:");
        library.displayAllMembersWithBorrowedBooks();
        library.displayAvailableBooks();

        // 返却テスト
        System.out.println("返却テスト:");
        library.receiveBookFromMember("M001", "978-4-00-310102-5");
        library.displayAllMembersWithBorrowedBooks();

        // シナリオ3: 検索機能のテスト
        System.out.println("=== シナリオ3: 検索機能のテスト ===");
        System.out.println("「夏目」で検索:");
        Book[] searchResults = library.searchBook("夏目");
        for (Book book : searchResults) {
            System.out.println(book.getBookDetails());
        }

        // シナリオ4: エラーケースのテスト
        System.out.println("=== シナリオ4: エラーケースのテスト ===");
        // 存在しないISBNでの貸出
        library.lendBookToMember("M001", "999-9-99-999999-9");
        
        // 貸出上限のテスト
        library.lendBookToMember("M002", "978-4-00-310105-6");
        library.lendBookToMember("M002", "978-4-00-310106-3");
        library.lendBookToMember("M002", "978-4-00-310107-0");
        library.lendBookToMember("M002", "978-4-00-310108-7"); // 上限超過

        // 貸出中の本の削除試行
        library.removeBook("978-4-00-310104-9"); // 貸出中の本

        // 会員退会のテスト
        library.unregisterMember("M001"); // 本を借りている会員
        library.unregisterMember("M002"); // 本を借りている会員
    }
} 