package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Domain.Voucher;
import com.example.demo.Respositories.VoucherRepos;
import com.example.demo.dto.Voucherdt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoucherServiceImp implements VoucherService{
    private final VoucherRepos voucherRepos;

    @Override
    public Voucher save(Voucherdt voucherdt, User user) {
        Voucher voucher = Voucher.builder()
                .value(voucherdt.getValue())
                .expireDate(voucherdt.getExpired_date())
                .check_range(voucherdt.getCheck_range())
                .min_ordervalue(voucherdt.getMin_ordervalue())
                .subtractPoint(voucherdt.getSubtractPoint())
                .description(voucherdt.getDescription())
                .userList(new ArrayList<>())
                .build();
        voucher.getUserList().add(user);
       return voucherRepos.save(voucher);
    }

    @Override
    public Voucher update(Voucher voucher, User user) {
        voucher.getUserList().add(user);
        return voucherRepos.save(voucher);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepos.findAll();
    }
}
