package jp.ac.ait.k24044.library;

//図書館システムの動作確認用メインクラス
//本の貸出・返却のシナリオを実行する
public class LibraryMain {
    public static void main(String[] args) {
        // 本の作成（10冊）
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

        // 会員の作成（2人）
        LibraryMember member1 = new LibraryMember("M001", "山田太郎");  // デフォルトの最大貸出冊数（5冊）
        LibraryMember member2 = new LibraryMember("M002", "鈴木花子", 3);  // 最大貸出冊数を3冊に設定

        // シナリオ1: 1冊の本を借りる
        System.out.println("=== シナリオ1: 1冊の本を借りる ===");
        member1.borrowBook(books[0]);
        member1.displayMemberInfo();
        System.out.println();

        // シナリオ2: 複数の本を借りる
        System.out.println("=== シナリオ2: 複数の本を借りる ===");
        Book[] booksToBorrow = {books[1], books[2], books[3]};
        int borrowedCount = member1.borrowBooks(booksToBorrow);
        System.out.println(borrowedCount + "冊の本を借りました。");
        member1.displayMemberInfo();
        System.out.println();

        // シナリオ3: 本を返却する
        System.out.println("=== シナリオ3: 本を返却する ===");
        member1.returnBook(books[0]);
        member1.displayMemberInfo();
        System.out.println();

        // シナリオ4: 貸出上限の確認
        System.out.println("=== シナリオ4: 貸出上限の確認 ===");
        Book[] moreBooks = {books[4], books[5], books[6], books[7]};
        borrowedCount = member1.borrowBooks(moreBooks);
        System.out.println(borrowedCount + "冊の本を借りました。");
        member1.displayMemberInfo();
        System.out.println();

        // シナリオ5: 別の会員による貸出
        System.out.println("=== シナリオ5: 別の会員による貸出 ===");
        member2.borrowBook(books[8]);
        member2.borrowBook(books[9]);
        member2.displayMemberInfo();
    }
} 