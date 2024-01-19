package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Domain.Voucher;
import com.example.demo.Respositories.VoucherRepos;
import com.example.demo.dto.Voucherdt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public void deleteExpiredVoucher() {
        List<Voucher> vouchers = voucherRepos.findAll();
        for(Voucher voucher : vouchers){
            if(voucher.getExpireDate().isBefore(LocalDate.now())){
                voucherRepos.delete(voucher);
            }
        }
    }

    @Override
    public Voucher deleteUser(Long Vid, Long Uid) {
        Voucher voucher = voucherRepos.getReferenceById(Vid);
        voucher.getUserList().removeIf(e ->e.getId() == Uid);
        return voucherRepos.save(voucher);
    }
}
