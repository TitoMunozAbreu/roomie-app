package es.roomie.household.kafka;

public record NewMemberInvitation(
        String title,
        String description,
        String memberEmail,
        String urlConfirmInvitation
) {}
