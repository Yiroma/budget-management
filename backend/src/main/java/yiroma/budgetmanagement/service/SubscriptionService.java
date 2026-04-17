package yiroma.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiroma.budgetmanagement.dto.SubscriptionResponse;
import yiroma.budgetmanagement.mapper.SubscriptionMapper;
import yiroma.budgetmanagement.repository.SubscriptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public List<SubscriptionResponse> getAll() {
        return subscriptionRepository.findAll().stream()
                .map(SubscriptionMapper::toResponse)
                .toList();
    }
}
