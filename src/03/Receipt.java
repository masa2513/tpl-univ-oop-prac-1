public class Receipt {
    ProductItem[] items;

    public Receipt(){
        //itemsを初期化する
        //itemsの要素数は0
        items = new ProductItem[0];
    }


    public void addProduct(ProductItem item){
       //引数で受け取ったProductItemのオブジェクトをitems配列に追加します。
         //itemsの要素数を1増やす
        ProductItem[] newItems = new ProductItem[items.length + 1];
        for(int i=0;i<items.length;i++){
            newItems[i] = items[i];
        }
        newItems[items.length] = item;
        items = newItems;
        }
    
    //合計算出
    public double getTotalPrice(){
        double money_num = 0;
        for(int i=0;i<items.length;i++){
            money_num += items[i].getSubtotal();
        }
        return money_num;
    }

    public int getTotalQuantity(){
        int material_num = 0;
        for(int i=0;i<items.length;i++){
            material_num += items[i].quantity;
        }
        return material_num;
    }

}
