package es.roomie.notification.kafka.household;

public record NewMemberInvitation(
        String title,
        String description,
        String memberEmail,
        String urlConfirmInvitation
) {}
