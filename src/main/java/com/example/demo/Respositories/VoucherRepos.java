package com.example.demo.Respositories;

import com.example.demo.Domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepos extends JpaRepository<Voucher,Long> {
}
