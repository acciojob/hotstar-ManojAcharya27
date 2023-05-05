package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay

        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setUser(userRepository.findById(subscriptionEntryDto.getUserId()).get());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        int totalAmount=0;
       // totalAmount=subscriptionEntryDto.getNoOfScreensRequired();
        if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.BASIC)){
            totalAmount+=500+200;
        }else if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.PRO)){
            totalAmount+=800+250;
        }else
            totalAmount=1000+(subscriptionEntryDto.getNoOfScreensRequired()*350);
        subscription.setTotalAmountPaid(totalAmount);
        subscriptionRepository.save(subscription);
        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        User user=userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();
        if(subscription.getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");

        }
        else if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            int screenRequired=subscription.getNoOfScreensSubscribed();
            int totalAmountRequiredForElite=1000+(screenRequired*350);
            int totalAmountOfPreviouslyPaid=subscription.getTotalAmountPaid();
            int remainingAmount=totalAmountRequiredForElite-totalAmountOfPreviouslyPaid;
            subscription.setTotalAmountPaid(totalAmountRequiredForElite);
            subscriptionRepository.save(subscription);
            return remainingAmount;
        }else{
            int screenRequired=subscription.getNoOfScreensSubscribed();
            int totalAmountRequiredForElite=1000+(screenRequired*350);
            int totalAmountOfPreviouslyPaid=subscription.getTotalAmountPaid();
            int remainingAmount=totalAmountRequiredForElite-totalAmountOfPreviouslyPaid;
            subscription.setTotalAmountPaid(totalAmountRequiredForElite);
            subscriptionRepository.save(subscription);
            return remainingAmount;
        }


    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription>subscriptions=subscriptionRepository.findAll();
        int totalRevenue=0;
        for(Subscription subscription: subscriptions){
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
