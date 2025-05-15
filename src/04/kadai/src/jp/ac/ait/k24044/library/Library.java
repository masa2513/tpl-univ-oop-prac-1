package jp.ac.ait.k24044.library;

public class Library {
    private Book[] books;
    private LibraryMember[] members;

    public Library() {
        this.books = new Book[0];
        this.members = new LibraryMember[0];
    }

    public boolean addBook(Book book) {
        if (book == null) {
            System.err.println("本の情報が無効です。");
            return false;
        }

        // 同じISBNの本が存在するかチェック
        for (Book existingBook : books) {
            if (existingBook.getIsbn().equals(book.getIsbn())) {
                System.err.println("同じISBNの本が既に存在します。");
                return false;
            }
        }

        // 配列を拡張して新しい本を追加
        Book[] newBooks = new Book[books.length + 1];
        System.arraycopy(books, 0, newBooks, 0, books.length);
        newBooks[books.length] = book;
        books = newBooks;
        return true;
    }

    public boolean removeBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            System.err.println("ISBNが無効です。");
            return false;
        }

        for (int i = 0; i < books.length; i++) {
            if (books[i].getIsbn().equals(isbn)) {
                if (books[i].isBorrowed()) {
                    System.err.println("この本は現在貸出中です。");
                    return false;
                }

                // 配列から本を削除
                Book[] newBooks = new Book[books.length - 1];
                System.arraycopy(books, 0, newBooks, 0, i);
                System.arraycopy(books, i + 1, newBooks, i, books.length - i - 1);
                books = newBooks;
                return true;
            }
        }

        System.err.println("指定されたISBNの本が見つかりません。");
        return false;
    }

    public Book findBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return null;
        }

        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    public boolean registerMember(LibraryMember member) {
        if (member == null) {
            System.err.println("会員情報が無効です。");
            return false;
        }

        // 同じ会員IDの会員が存在するかチェック
        for (LibraryMember existingMember : members) {
            if (existingMember.getMemberId().equals(member.getMemberId())) {
                System.err.println("同じ会員IDの会員が既に存在します。");
                return false;
            }
        }

        // 配列を拡張して新しい会員を追加
        LibraryMember[] newMembers = new LibraryMember[members.length + 1];
        System.arraycopy(members, 0, newMembers, 0, members.length);
        newMembers[members.length] = member;
        members = newMembers;
        return true;
    }

    public boolean unregisterMember(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            System.err.println("会員IDが無効です。");
            return false;
        }

        for (int i = 0; i < members.length; i++) {
            if (members[i].getMemberId().equals(memberId)) {
                if (members[i].getCurrentBorrowCount() > 0) {
                    System.err.println("この会員は本を貸出中です。");
                    return false;
                }

                // 配列から会員を削除
                LibraryMember[] newMembers = new LibraryMember[members.length - 1];
                System.arraycopy(members, 0, newMembers, 0, i);
                System.arraycopy(members, i + 1, newMembers, i, members.length - i - 1);
                members = newMembers;
                return true;
            }
        }

        System.err.println("指定された会員IDの会員が見つかりません。");
        return false;
    }

    public LibraryMember findMemberById(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            return null;
        }

        for (LibraryMember member : members) {
            if (member.getMemberId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    public boolean lendBookToMember(String memberId, String isbn) {
        LibraryMember member = findMemberById(memberId);
        if (member == null) {
            System.err.println("指定された会員が見つかりません。");
            return false;
        }

        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.err.println("指定された本が見つかりません。");
            return false;
        }

        return member.borrowBook(book);
    }

    public boolean receiveBookFromMember(String memberId, String isbn) {
        LibraryMember member = findMemberById(memberId);
        if (member == null) {
            System.err.println("指定された会員が見つかりません。");
            return false;
        }

        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.err.println("指定された本が見つかりません。");
            return false;
        }

        return member.returnBook(book);
    }

    public Book[] searchBook(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new Book[0];
        }

        keyword = keyword.toLowerCase();
        int count = 0;
        Book[] matchingBooks = new Book[books.length];

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword) ||
                book.getAuthor().toLowerCase().contains(keyword)) {
                matchingBooks[count++] = book;
            }
        }

        Book[] result = new Book[count];
        System.arraycopy(matchingBooks, 0, result, 0, count);
        return result;
    }

    public void displayAllBooks() {
        System.out.println("=== 全蔵書一覧 ===");
        for (Book book : books) {
            System.out.println(book.getBookDetails());
        }
        System.out.println();
    }

    public void displayAvailableBooks() {
        System.out.println("=== 貸出可能な本の一覧 ===");
        for (Book book : books) {
            if (!book.isBorrowed()) {
                System.out.println(book.getBookDetails());
            }
        }
        System.out.println();
    }

    public void displayAllMembersWithBorrowedBooks() {
        System.out.println("=== 全会員と貸出中の本の一覧 ===");
        for (LibraryMember member : members) {
            member.displayMemberInfo();
            System.out.println();
        }
    }
} 