package com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Services.SellerService;

import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.CustomizeErrorHandling.ECommerceApplicationException;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Model.AddressUpdateDto;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Email.EmailSenderService.EmailSenderService;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Domain.Address;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Domain.Seller;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Domain.User;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Model.UserProfileDto;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Repositories.AddressRepository.AddressRepository;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Repositories.SellerRepository.SellerRepository;
import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Repositories.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    @Autowired
    private SellerRepository   sellerRepository;
    @Autowired
    private UserRepository     userRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AddressRepository  addressRepository;

    public Seller findOne(Long Id) {
        Seller seller = sellerRepository.findById(Id)
                                        .orElseThrow(() -> new ECommerceApplicationException("No user found with id :"
                                                                                             + " " + Id));
        return seller;
    }

    public Page<Seller> findAllSellers() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "Id");
        Page<Seller> result = sellerRepository
                .findAll(pageable);
        return result;
    }

    public String SellerActivate(Long Id) {
        Seller seller = sellerRepository.findById(Id)
                                        .orElseThrow(() -> new ECommerceApplicationException("No user found with id :"
                                                                                             + " " + Id));
        if (seller.getUser().getIsActive().equals(Boolean.FALSE)) {
            seller.getUser().setIsActive(Boolean.TRUE);
            sellerRepository.save(seller);
            emailSenderService.sendMail(
                    seller.getUser().getEmail(),
                    "Account Activated",
                    "Account has been activated"
                                       );

            return "Seller Activated";

        }
        else {
            return "Already Activated";
        }
    }

    public String SellerDeActivate(Long Id) {
        {
            Seller seller = sellerRepository.findById(Id)
                                            .orElseThrow(() -> new ECommerceApplicationException("No user found with "
                                                                                                 + "id : " + Id));
            if (seller.getUser().getIsActive().equals(Boolean.TRUE)) {
                seller.getUser().setIsActive(Boolean.FALSE);
                sellerRepository.save(seller);

                emailSenderService.sendMail(
                        seller.getUser().getEmail(),
                        "Account Deactivated",
                        "Account has been Deactivated"
                                           );


            }
            return "Seller Deactivated";
        }
    }

    public Seller getSellerProfile(String name) {

        User user = userRepository.findByEmail(name)
                                  .orElseThrow(() -> new ECommerceApplicationException("No user found"));

        Seller seller = sellerRepository.findByUser(user)
                                        .orElseThrow(() -> new ECommerceApplicationException("No user found"));
        return seller;
    }

    public void updateMyAddress(String name, AddressUpdateDto addressUpdateDto) {
        User user = userRepository.findByEmail(name)
                                  .orElseThrow(() -> new ECommerceApplicationException("No user found"));

        Seller seller = sellerRepository.findByUser(user)
                                        .orElseThrow(() -> new ECommerceApplicationException("No user found"));

        Address address = seller.getUser().getAddressSet().stream()
                                .findFirst()
                                .orElseThrow(() -> new ECommerceApplicationException("No address found for seller"));

        if (addressUpdateDto.getCity() != null)
            address.setCity(addressUpdateDto.getCity());

        if (addressUpdateDto.getState() != null)
            address.setState(addressUpdateDto.getState());

        if (addressUpdateDto.getLabel() != null)
            address.setLabel(addressUpdateDto.getLabel());

        if (addressUpdateDto.getAddressLine() != null)
            address.setAddressLine(addressUpdateDto.getAddressLine());

        if (addressUpdateDto.getZipCode() != null)
            address.setZipCode(addressUpdateDto.getZipCode());

        if (addressUpdateDto.getCountry() != null)
            address.setCountry(addressUpdateDto.getCountry());
        addressRepository.save(address);
    }

    public void UpdateMyprofile(String name, UserProfileDto userProfileDto) {

        User user = userRepository.findByEmail(name)
                                  .orElseThrow(() -> new ECommerceApplicationException("No user found"));

        Seller seller = sellerRepository.findByUser(user)
                                        .orElseThrow(() -> new ECommerceApplicationException("No user found"));

        if (userProfileDto.getFirstName() != null)
            seller.getUser().setFirstName(userProfileDto.getFirstName());
        if (userProfileDto.getMiddleName() != null)
            seller.getUser().setMiddleName(userProfileDto.getMiddleName());
        if (userProfileDto.getLastName() != null)
            seller.getUser().setLastName(userProfileDto.getLastName());
        if (userProfileDto.getEmail() != null)
            seller.getUser().setEmail(userProfileDto.getEmail());
        if (userProfileDto.getContactNo() != null)
            seller.setCompany_Contact(userProfileDto.getContactNo());

        sellerRepository.save(seller);
    }
}

