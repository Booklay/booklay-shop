package com.nhnacademy.booklay.server.repository.delivery;

import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.QDeliveryDestination;
import com.nhnacademy.booklay.server.entity.QGender;
import com.nhnacademy.booklay.server.entity.QMember;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class DeliveryDestinationRepositoryRepositoryImpl extends QuerydslRepositorySupport
    implements
    DeliveryDestinationRepositoryCustom {
    public DeliveryDestinationRepositoryRepositoryImpl() {
        super(DeliveryDestination.class);
    }

    @Override
    public List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinationByMemberNo(
        Long memberNo) {
        QDeliveryDestination deliveryDestination = QDeliveryDestination.deliveryDestination;
        QMember member = QMember.member;
        QGender gender = QGender.gender;

        return from(deliveryDestination)
            .innerJoin(member).on(deliveryDestination.member.memberNo.eq(member.memberNo))
            .innerJoin(gender).on(member.gender.id.eq(gender.id))
            .where(deliveryDestination.member.memberNo.eq(memberNo))
            .select(Projections.constructor(DeliveryDestinationRetrieveResponse.class,
                deliveryDestination.id,
                member.memberNo,
                deliveryDestination.name,
                deliveryDestination.zipCode,
                deliveryDestination.address,
                deliveryDestination.isDefaultDestination))
            .fetch();
    }
}
