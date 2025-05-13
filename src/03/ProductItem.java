public class ProductItem {
    public String  productName;
    public double  unitPrice;
    public int  quantity;
    public String jancode;
    //小計を計算するメソッド
    public double getSubtotal(){
        return this.unitPrice * this.quantity;
    }
    // その商品情報をレシートの1行として表示するための整形済み文字列
    public String toString(){
        String ret = String.format("商品名:%s , 単価:%.2f, 数量:%d, 小計:%.0f%n",
        productName, unitPrice, quantity, getSubtotal());
        return ret;
    }

    public ProductItem(String productName, double unitPrice, int quantity, String jancode) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.jancode = jancode;
    }
}
