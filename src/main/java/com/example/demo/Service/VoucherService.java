package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Domain.Voucher;
import com.example.demo.dto.Voucherdt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoucherService {
    public Voucher save(Voucherdt voucherdt, User user);
    public Voucher update(Voucher voucher , User user);

    List<Voucher> getAllVouchers();
}
