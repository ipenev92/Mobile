package com.example.androidgames.activities.balatro.components;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Voucher {
    private final String name;
    private String text;
    private String price;

    private static final Map<String, VoucherData> VOUCHER_MAP = new HashMap<>();

    static {
        VOUCHER_MAP.put("voucher_grabber", new VoucherData("Permanently gain +1 hand"
                , "10"));
        VOUCHER_MAP.put("voucher_overstock", new VoucherData("+1 card slot available" +
                " in shop", "10"));
        VOUCHER_MAP.put("voucher_wasteful", new VoucherData("Permanently gain +1 discard",
                "10"));
    }

    public Voucher(String name) {
        this.name = name;
        this.updateVoucher();
    }

    private void updateVoucher() {
        VoucherData data = VOUCHER_MAP.get(this.name);
        if (data != null) {
            this.text = data.getText();
            this.price = data.getPrice();
        }
    }

    @Getter
    private static class VoucherData {
        private final String text;
        private final String price;

        public VoucherData(String text, String price) {
            this.text = text;
            this.price = price;
        }
    }
}