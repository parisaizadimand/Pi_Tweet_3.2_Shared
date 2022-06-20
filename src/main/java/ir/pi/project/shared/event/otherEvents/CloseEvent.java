package ir.pi.project.shared.event.otherEvents;

import ir.pi.project.shared.event.Event;
import ir.pi.project.shared.event.EventVisitor;
import ir.pi.project.shared.response.Response;

public class CloseEvent extends Event {
    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.closeEvent();
    }
}
