/**
 * Created by Dai Mai on 7/13/17.
 */

export class DateUtil {
    addDays(date: Date, days: number): Date {
        const result: Date = new Date(date);
        result.setDate(result.getDate() + days);
        return result;
    }
}
