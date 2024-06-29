package com.example.foodorder.helper;
import android.content.Context;
import android.widget.Toast;

import com.example.foodorder.domain.Foods;

import java.util.ArrayList;


public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB=new TinyDB(context);
    }

    // thêm món ăn vào giỏ hàng
    public void insertFood(Foods item) {
        // lấy danh sách món ăn trong giỏ hàng
        ArrayList<Foods> listpop = getListCart();
        // kiểm tra xem món ăn đã tồn tại trong giỏ hàng chưa
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        }else{
            listpop.add(item);
        }
        // lưu danh sách món ăn vào giỏ hàng
        tinyDB.putListObject("CartList",listpop);
        Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Foods> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    // lấy tổng số lượng món ăn trong giỏ hàng
    public Double getTotalFee(){
        ArrayList<Foods> listItem=getListCart();
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }
    // hàm giảm số lượng món ăn trong giỏ hàng
    public void minusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    //  hàm tăng số lượng món ăn trong giỏ hàng
    public  void plusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    // hàm xóa món ăn trong giỏ hàng
    public  void removeItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.remove(position);
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }

    public void clearCart() {
        tinyDB.putListObject("CartList", new ArrayList<Foods>());
    }


}
