import {getAnnouncementList} from "@/api/system/announcement";

// 公告store
export const useAnnouncementStore = defineStore("announcement", {
    state: () => ({
        list: [],
        closedIds: JSON.parse(localStorage.getItem("closedAnnouncements") || "[]")
    }),
    actions: {
        async fetchValidAnnouncements() {
            const res = await getAnnouncementList();
            this.list = res.data.filter(ann => {
                return !this.closedIds.includes(ann.announcementId)
            });
        },
        closeAnnouncement(announcementId) {
            if (!this.closedIds.includes(announcementId)) {
                this.closedIds.push(announcementId);
                localStorage.setItem("closedAnnouncements", JSON.stringify(this.closedIds));
                this.list = this.list.filter(ann => ann.announcementId !== announcementId);
            }
        }
    }
});