package com.epam.esm.hateoas;

import com.epam.esm.controller.GiftCertificatesController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.utils.GiftCertificateQueryParameters;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificatesHateoasIssuer extends AbstractHateoasIssuer<GiftCertificateDto> {

    private static final Class<GiftCertificatesController> GIFT_CERTIFICATE_CONTROLLER = GiftCertificatesController.class;

    public void addGiftCertificateLink(GiftCertificateDto giftCertificate) {
        addLinks(GIFT_CERTIFICATE_CONTROLLER, giftCertificate, giftCertificate.getId(), SELF_LINK);
    }

    public CollectionModel<GiftCertificateDto> addGiftCertificateLinks(List<GiftCertificateDto> giftCertificateDtos, GiftCertificateQueryParameters parameters, Integer page, Integer pageSize, boolean hasNextPage) {
        for (GiftCertificateDto giftCertificateDto : giftCertificateDtos) {
            addGiftCertificateLink(giftCertificateDto);
        }
        return addPagesLinks(giftCertificateDtos, parameters, page, pageSize, hasNextPage);
    }

    private CollectionModel<GiftCertificateDto> addPagesLinks(List<GiftCertificateDto> giftCertificateDtos, GiftCertificateQueryParameters parameters, Integer page, Integer pageSize, boolean hasNextPage) {
        Link previousPage = linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER)
                            .getGiftCertificates(evaluatePreviousPage(page), pageSize, parameters)).withSelfRel();
        Link nextPage = linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER)
                            .getGiftCertificates(evaluateNextPage(page, hasNextPage), pageSize, parameters)).withSelfRel();
        return CollectionModel.of(giftCertificateDtos, previousPage, nextPage);
    }



}
